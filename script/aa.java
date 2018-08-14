package cc.aa;

import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

public class UnderstandDispatchTouchEvent {
    /**
     * dispatchTouchEvent()源码学习及其注释
     * 常说事件传递中的流程是:dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
     * 在这个链条中dispatchTouchEvent()是处在链首的位置当然也是最重要的.
     * 在dispatchTouchEvent()决定了Touch事件是由自己的onTouchEvent()处理
     * 还是分发给子View处理让子View调用其自身的dispatchTouchEvent()处理.
     * 
     *
     * 其实dispatchTouchEvent()和onInterceptTouchEvent()以及onTouchEvent()的关系
     * 在dispatchTouchEvent()方法的源码中体现得很明显.
     * 比如dispatchTouchEvent()会调用onInterceptTouchEvent()来判断是否要拦截.
     * 比如dispatchTouchEvent()会调用dispatchTransformedTouchEvent()方法且在该方法中递归调用
     * dispatchTouchEvent();从而会在dispatchTouchEvent()里最终调用到onTouchEvent()
     * 
     * 
     * 
     * 重点关注:
     * 1 子View对于ACTION_DOWN的处理十分重要!!!!!
     *   ACTION_DOWN是一系列Touch事件的开端,如果子View对于该ACTION_DOWN事件在onTouchEvent()中返回了false即未消费.
     *   那么ViewGroup就不会把后续的ACTION_MOVE和ACTION_UP派发给该子View.在这种情况下ViewGroup就和普通的View一样了,
     *   调用该ViewGroup自己的dispatchTouchEvent()从而调用自己的onTouchEvent();即不会将事件分发给子View.
     *   详细代码请参见如下代码分析.
     *   
     * 2 为什么子view对于Touch事件处理返回true那么其上层的ViewGroup就无法处理Touch事件了?????
     *   这个想必大家都知道了,因为该Touch事件被子View消费了其上层的ViewGroup就无法处理该Touch事件了.
     *   那么在源码中的依据是什么呢??请看下面的源码分析
     *   
     * 参考资料:
     *   Thank you very much
     */
	
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(ev, 1);
        }

        boolean handled = false;
        if (onFilterTouchEventForSecurity(ev)) {
            final int action = ev.getAction();
            final int actionMasked = action & MotionEvent.ACTION_MASK;

            /**
             * 第一步:对于ACTION_DOWN进行处理(Handle an initial down)
             * 因为ACTION_DOWN是一系列事件的开端,当是ACTION_DOWN时进行一些初始化操作.
             * 从源码的注释也可以看出来:清除以往的Touch状态(state)开始新的手势(gesture)
             * cancelAndClearTouchTargets(ev)中有一个非常重要的操作:
             * 将mFirstTouchTarget设置为null!!!!
             * 随后在resetTouchState()中重置Touch状态标识
             */
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // Throw away all previous state when starting a new touch gesture.
                // The framework may have dropped the up or cancel event for the previous gesture
                // due to an app switch, ANR, or some other state change.
                cancelAndClearTouchTargets(ev);
                resetTouchState();
            }

            
			/**
			 * 第二步:检查是否要拦截(Check for interception)
			 * 在dispatchTouchEvent(MotionEventev)这段代码中
			 * 使用变量intercepted来标记ViewGroup是否拦截Touch事件的传递.
			 * 该变量在后续代码中起着很重要的作用.
			 */
            final boolean intercepted;
			// 事件为ACTION_DOWN或者mFirstTouchTarget不为null(即已经找到能够接收touch事件的目标组件)时if成立
            if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null) {
            	//判断disallowIntercept(禁止拦截)标志位
				//因为在其他地方可能调用了requestDisallowInterceptTouchEvent(boolean disallowIntercept)
				//从而禁止执行是否需要拦截的判断(有点拗口~其实看requestDisallowInterceptTouchEvent()方法名就可明白)
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                //当没有禁止拦截判断时(即disallowIntercept为false)调用onInterceptTouchEvent(ev)方法
                if (!disallowIntercept) {
                	//既然disallowIntercept为false那么就调用onInterceptTouchEvent()方法将结果赋值给intercepted
                	//常说事件传递中的流程是:dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
                	//其实在这就是一个体现,在dispatchTouchEvent()中调用了onInterceptTouchEvent()
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                	 //当禁止拦截判断时(即disallowIntercept为true)设置intercepted = false
                    intercepted = false;
                }
            } else {
            	//当事件不是ACTION_DOWN并且mFirstTouchTarget为null(即没有Touch的目标组件)时
            	//设置 intercepted = true表示ViewGroup执行Touch事件拦截的操作。
                //There are no touch targets and this action is not an initial down
                //so this view group continues to intercept touches.
                intercepted = true;
            }

            
            /**
             * 第三步:检查cancel(Check for cancelation)
             * 
             */
            final boolean canceled = resetCancelNextUpFlag(this) || actionMasked == MotionEvent.ACTION_CANCEL;

            
            /**
             * 第四步:事件分发(Update list of touch targets for pointer down, if needed)
             */
            final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;
            TouchTarget newTouchTarget = null;
            boolean alreadyDispatchedToNewTouchTarget = false;
            //不是ACTION_CANCEL并且ViewGroup的拦截标志位intercepted为false(不拦截)
            if (!canceled && !intercepted) {
            	//处理ACTION_DOWN事件.这个环节比较繁琐.
                if (actionMasked == MotionEvent.ACTION_DOWN
                    || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                    final int actionIndex = ev.getActionIndex(); // always 0 for down
                    final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex):TouchTarget.ALL_POINTER_IDS;

                    // Clean up earlier touch targets for this pointer id in case they
                    // have become out of sync.
                    removePointersFromTouchTargets(idBitsToAssign);

                    final int childrenCount = mChildrenCount;
                    if (childrenCount != 0) {
                    	// 依据Touch坐标寻找子View来接收Touch事件
                        // Find a child that can receive the event.
                        // Scan children from front to back.
                        final View[] children = mChildren;
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);

                        final boolean customOrder = isChildrenDrawingOrderEnabled();
                        // 遍历子View判断哪个子View接受Touch事件
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            final int childIndex = customOrder ? getChildDrawingOrder(childrenCount, i) : i;
                            final View child = children[childIndex];
                            if (!canViewReceivePointerEvents(child) || !isTransformedTouchPointInView(x, y, child, null)) {
                                continue;
                            }

                            newTouchTarget = getTouchTarget(child);
                            if (newTouchTarget != null) {
                            	// 找到接收Touch事件的子View!!!!!!!即为newTouchTarget.
                            	// 既然已经找到了,所以执行break跳出for循环
                                // Child is already receiving touch within its bounds.
                                // Give it the new pointer in addition to the ones it is handling.
                                newTouchTarget.pointerIdBits |= idBitsToAssign;
                                break;
                            }

                            resetCancelNextUpFlag(child);
                            /**
                             * 如果上面的if不满足,当然也不会执行break语句.
                             * 于是代码会执行到这里来.
                             * 
                             * 调用方法dispatchTransformedTouchEvent()将Touch事件传递给子View做
                             * 递归处理(也就是遍历该子View的View树)
                             * 该方法很重要,看一下源码中关于该方法的描述:
                             * Transforms a motion event into the coordinate space of a particular child view,
                             * filters out irrelevant pointer ids, and overrides its action if necessary.
                             * If child is null, assumes the MotionEvent will be sent to this ViewGroup instead.
                             * 将Touch事件传递给特定的子View.
                             * 该方法十分重要!!!!在该方法中为一个递归调用,会递归调用dispatchTouchEvent()方法！！！！！！！！！！！！！！
                             * 在dispatchTouchEvent()中:
                             * 如果子View为ViewGroup并且Touch没有被拦截那么递归调用dispatchTouchEvent()
                             * 如果子View为View那么就会调用其onTouchEvent(),这个就不再赘述了.
                             * 
                             * 
                             * 该方法返回true则表示子View消费掉该事件,同时进入该if判断.
                             * 满足if语句后重要的操作有:
                             * 1 给newTouchTarget赋值
                             * 2 给alreadyDispatchedToNewTouchTarget赋值为true.
                             *   看这个比较长的英语名字也可知其含义:已经将Touch派发给新的TouchTarget
                             * 3 执行break.
                             *   因为该for循环遍历子View判断哪个子View接受Touch事件,既然已经找到了
                             *   那么就跳出该for循环.
                             * 4 注意:
                             *   如果dispatchTransformedTouchEvent()返回false即子View
                             *   的onTouchEvent返回false(即Touch事件未被消费)那么就不满足该if条件,也就无法执行addTouchTarget()
                             *   从而导致mFirstTouchTarget为null.那么该子View就无法继续处理ACTION_MOVE事件
                             *   和ACTION_UP事件!!!!!!!!!!!!!!!!!!!!!!
                             * 5 注意:
                             *   如果dispatchTransformedTouchEvent()返回true即子View
                             *   的onTouchEvent返回true(即Touch事件被消费)那么就满足该if条件.
                             *   从而mFirstTouchTarget不为null!!!!!!!!!!!!!!!!!!!
                             * 6 小结:
                             *   对于此处ACTION_DOWN的处理具体体现在dispatchTransformedTouchEvent()
                             *   该方法返回boolean,如下:
                             *   true---->事件被消费----->mFirstTouchTarget!=null
                             *   false--->事件未被消费---->mFirstTouchTarget==null
                             *   因为在dispatchTransformedTouchEvent()会调用递归调用dispatchTouchEvent()和onTouchEvent()
                             *   所以dispatchTransformedTouchEvent()的返回值实际上是由onTouchEvent()决定的.
                             *   简单地说onTouchEvent()是否消费了Touch事件(true or false)的返回值决定了dispatchTransformedTouchEvent()
                             *   的返回值!!!!!!!!!!!!!从而决定了mFirstTouchTarget是否为null!!!!!!!!!!!!!!!!从而进一步决定了ViewGroup是否
                             *   处理Touch事件.这一点在下面的代码中很有体现.
                             *   
                             * 
                             */
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                mLastTouchDownIndex = childIndex;
                                mLastTouchDownX = ev.getX();
                                mLastTouchDownY = ev.getY();
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }
                        }
                    }

                    
                    /**
                     * 该if条件表示:
                     * 经过前面的for循环没有找到子View接收Touch事件并且之前的mFirstTouchTarget不为空
                     */
                    if (newTouchTarget == null && mFirstTouchTarget != null) {
                        // Did not find a child to receive the event.
                        // Assign the pointer to the least recently added target.
                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        //newTouchTarget指向了最初的TouchTarget
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }

            
            
            /**
             * 分发Touch事件至target(Dispatch to touch targets)
             * 
             * 经过上面对于ACTION_DOWN的处理后mFirstTouchTarget有两种情况:
             * 1 mFirstTouchTarget为null
             * 2 mFirstTouchTarget不为null
             * 
             * 当然如果不是ACTION_DOWN就不会经过上面较繁琐的流程
             * 而是从此处开始执行,比如ACTION_MOVE和ACTION_UP
             */
            if (mFirstTouchTarget == null) {
            	/**
            	 * 情况1：mFirstTouchTarget为null
            	 * 
            	 * 经过上面的分析mFirstTouchTarget为null就是说Touch事件未被消费.
            	 * 即没有找到能够消费touch事件的子组件或Touch事件被拦截了，
            	 * 则调用ViewGroup的dispatchTransformedTouchEvent()方法处理Touch事件则和普通View一样.
            	 * 即子View没有消费Touch事件,那么子View的上层ViewGroup才会调用其onTouchEvent()处理Touch事件.
            	 * 在源码中的注释为:No touch targets so treat this as an ordinary view.
            	 * 也就是说此时ViewGroup像一个普通的View那样调用dispatchTouchEvent(),且在dispatchTouchEvent()
            	 * 中会去调用onTouchEvent()方法.
            	 * 具体的说就是在调用dispatchTransformedTouchEvent()时第三个参数为null.
            	 * 第三个参数View child为null会做什么样的处理呢?
            	 * 请参见下面dispatchTransformedTouchEvent()的源码分析
            	 * 
            	 * 这就是为什么子view对于Touch事件处理返回true那么其上层的ViewGroup就无法处理Touch事件了!!!!!!!!!!
            	 * 这就是为什么子view对于Touch事件处理返回false那么其上层的ViewGroup才可以处理Touch事件!!!!!!!!!!
            	 * 
            	 */
                handled = dispatchTransformedTouchEvent(ev, canceled, null,TouchTarget.ALL_POINTER_IDS);
            } else {
            	/**
            	 * 情况2：mFirstTouchTarget不为null即找到了可以消费Touch事件的子View且后续Touch事件可以传递到该子View
            	 * 在源码中的注释为:
            	 * Dispatch to touch targets, excluding the new touch target if we already dispatched to it.  
            	 * Cancel touch targets if necessary.
            	 */
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else {
                        final boolean cancelChild = resetCancelNextUpFlag(target.child) || intercepted;
                        //对于非ACTION_DOWN事件继续传递给目标子组件进行处理,依然是递归调用dispatchTransformedTouchEvent()
                        if (dispatchTransformedTouchEvent(ev, cancelChild, target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        if (cancelChild) {
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }

            /**
             * 处理ACTION_UP和ACTION_CANCEL
             * Update list of touch targets for pointer up or cancel, if needed.
             * 在此主要的操作是还原状态
             */
            if (canceled|| actionMasked == MotionEvent.ACTION_UP
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                resetTouchState();
            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
                final int actionIndex = ev.getActionIndex();
                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
                removePointersFromTouchTargets(idBitsToRemove);
            }
        }

        if (!handled && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
        }
        return handled;
    }
    
    
    
    //=====================以上为dispatchTouchEvent()源码分析======================
    
    
    
    //===============以下为dispatchTransformedTouchEvent()源码分析=================
    
    /**
     * 在dispatchTouchEvent()中调用dispatchTransformedTouchEvent()将事件分发给子View处理
     * 
     * Transforms a motion event into the coordinate space of a particular child view,
     * filters out irrelevant pointer ids, and overrides its action if necessary.
     * If child is null, assumes the MotionEvent will be sent to this ViewGroup instead.
     * 
     * 在此请着重注意第三个参数:View child
     * 在dispatchTouchEvent()中多次调用了dispatchTransformedTouchEvent(),但是有时候第三个参数为null,有时又不是.
     * 那么这个参数是否为null有什么区别呢？
     * 在如下dispatchTransformedTouchEvent()源码中可见多次对于child是否为null的判断,并且均做出如下类似的操作:
     * if (child == null) {
     *       handled = super.dispatchTouchEvent(event);
     *    } else {
     *       handled = child.dispatchTouchEvent(event);
     * }
     * 这个代码是什么意思呢??
     * 当child == null时会将Touch事件传递给该ViewGroup自身的dispatchTouchEvent()处理.
     * 即super.dispatchTouchEvent(event)正如源码中的注释描述的一样:
     * If child is null, assumes the MotionEvent will be sent to this ViewGroup instead.
     * 当child != null时会调用该子view(当然该view可能是一个View也可能是一个ViewGroup)的dispatchTouchEvent(event)处理.
     * 即child.dispatchTouchEvent(event);
     * 
     * 
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent event,boolean cancel,View child,int desiredPointerIdBits) {
        final boolean handled;

        // Canceling motions is a special case.  We don't need to perform any transformations
        // or filtering.  The important part is the action, not the contents.
        final int oldAction = event.getAction();
        if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }

        // Calculate the number of pointers to deliver.
        final int oldPointerIdBits = event.getPointerIdBits();
        final int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;

        // If for some reason we ended up in an inconsistent state where it looks like we
        // might produce a motion event with no pointers in it, then drop the event.
        if (newPointerIdBits == 0) {
            return false;
        }

        // If the number of pointers is the same and we don't need to perform any fancy
        // irreversible transformations, then we can reuse the motion event for this
        // dispatch as long as we are careful to revert any changes we make.
        // Otherwise we need to make a copy.
        final MotionEvent transformedEvent;
        if (newPointerIdBits == oldPointerIdBits) {
            if (child == null || child.hasIdentityMatrix()) {
                if (child == null) {
                    handled = super.dispatchTouchEvent(event);
                } else {
                    final float offsetX = mScrollX - child.mLeft;
                    final float offsetY = mScrollY - child.mTop;
                    event.offsetLocation(offsetX, offsetY);

                    handled = child.dispatchTouchEvent(event);

                    event.offsetLocation(-offsetX, -offsetY);
                }
                return handled;
            }
            transformedEvent = MotionEvent.obtain(event);
        } else {
            transformedEvent = event.split(newPointerIdBits);
        }

        // Perform any necessary transformations and dispatch.
        if (child == null) {
            handled = super.dispatchTouchEvent(transformedEvent);
        } else {
            final float offsetX = mScrollX - child.mLeft;
            final float offsetY = mScrollY - child.mTop;
            transformedEvent.offsetLocation(offsetX, offsetY);
            if (! child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }

            handled = child.dispatchTouchEvent(transformedEvent);
        }

        // Done.
        transformedEvent.recycle();
        return handled;
    }