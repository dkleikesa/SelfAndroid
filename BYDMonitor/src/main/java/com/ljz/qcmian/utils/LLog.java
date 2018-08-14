package com.ljz.qcmian.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LLog {
    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    Config mConfig = null;
    IPrinter mPrinter = null;
    IWriter mWriter = null;

    private static final SimpleDateFormat TIME_LINE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public LLog(Config config, IPrinter printer, IWriter writer) throws Exception {
        mConfig = new Config(config);
        if (printer == null) {
            mPrinter = new DefaultPrinter();
        } else {
            mPrinter = printer;
        }
        if ((writer == null) && config.mPrint) {
            throw new Exception("writer is null");
        }
        mWriter = writer;
    }


    public void v(String tag, String line) {
        v(tag, line, false);
    }

    public void v(String tag, String line, boolean save) {
        addLog(VERBOSE, tag, line, save);
    }

    public void v(String tag, String line, long privateTime) {
        v(tag, line, privateTime, false);
    }

    public void v(String tag, String line, long privateTime, boolean save) {
        addLog(VERBOSE, tag, line, privateTime, save);
    }


    public void d(String tag, String line) {
        d(tag, line, false);
    }

    public void d(String tag, String line, boolean save) {
        addLog(DEBUG, tag, line, save);
    }

    public void d(String tag, String line, long privateTime) {
        d(tag, line, privateTime, false);
    }

    public void d(String tag, String line, long privateTime, boolean save) {
        addLog(DEBUG, tag, line, privateTime, save);
    }

    public void i(String tag, String line) {
        i(tag, line, false);
    }

    public void i(String tag, String line, boolean save) {
        addLog(INFO, tag, line, save);
    }

    public void i(String tag, String line, long privateTime) {
        i(tag, line, privateTime, false);
    }

    public void i(String tag, String line, long privateTime, boolean save) {
        addLog(INFO, tag, line, privateTime, save);
    }

    public void w(String tag, String line) {
        w(tag, line, false);
    }

    public void w(String tag, String line, boolean save) {
        addLog(WARN, tag, line, save);
    }

    public void w(String tag, String line, long privateTime) {
        w(tag, line, privateTime, false);
    }

    public void w(String tag, String line, long privateTime, boolean save) {
        addLog(WARN, tag, line, privateTime, save);
    }

    public void e(String tag, String line) {
        e(tag, line, false);
    }

    public void e(String tag, String line, boolean save) {
        addLog(ERROR, tag, line, save);
    }

    public void e(String tag, String line, long privateTime) {
        e(tag, line, privateTime, false);
    }

    public void e(String tag, String line, long privateTime, boolean save) {
        addLog(ERROR, tag, line, privateTime, save);
    }

    public void e(Throwable e) {
        e(e, false);
    }

    public void e(Throwable e, boolean save) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        e("EXCEPTION", sw.toString(), save);
    }

    public synchronized void addLog(int priority, String tag, String line, boolean save) {
        if (mConfig.mPrint) {
            if (isEmpty(mConfig.tag)) {
                mPrinter.print(priority, tag, line);
            } else {
                mPrinter.print(priority, mConfig.tag, "[" + tag + "] " + line);
            }

        }
        if (mConfig.mSaveFile && save) {
            mWriter.appendln(getStringPriority(priority) + "/[" + tag + "] " + line);
        }
    }

    public void addLog(int priority, String tag, String line, long time, boolean save) {
        line = TIME_LINE.format(new Date(time)) + "->" + line;
        addLog(priority, tag, line, save);

    }

    public void flush() {
        if (mWriter != null)
            mWriter.flush();
    }

    public interface IWriter {
        public void appendln(String msg);

        public void flush();
    }

    public static class DefaultWriter implements IWriter {
        private volatile PrintStream mPs = null;
        private volatile File mfile = null;
        private Config mConfig = null;


        public DefaultWriter(Config config) throws Exception {
            mConfig = new Config(config);
        }

        @Override
        public void appendln(String msg) {
            try {
                makePs();
                if (mPs != null) {
                    mPs.append(msg);
                    mPs.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void flush() {
            if (mPs != null) {
                mPs.flush();
            }
        }

        private void makePs() throws Exception {

            mfile = new File(mConfig.mLogPathFolder, mConfig.mFileName);

            if (!mfile.exists()) {
                if (mPs != null) {
                    mPs.close();
                    mPs = null;
                }
                File file = new File(mConfig.mLogPathFolder);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("file.mkdirs err:" + mConfig.mLogPathFolder);
                    }
                } else if (!file.isDirectory()) {
                    throw new IOException("file is not directory:" + mConfig.mLogPathFolder);
                }
                mfile.createNewFile();
            }
            checkFull();

            if (mPs == null) {
                mPs = new PrintStream(new FileOutputStream(mfile, true), true);
            }
        }

        private void checkFull() {
            if (mfile.length() >= mConfig.mLogFileLength) {
                if (mPs != null) {
                    mPs.flush();
                    mPs.close();
                    mPs = null;
                }
                try {
                    File file = new File(mConfig.mLogPathFolder);
                    int maxIndex = 0;
                    List<String> fList = new ArrayList<>();

                    String[] names = file.list();
                    for (String s : names) {
                        int len = s.length();
                        if ((s.indexOf(mfile.getName()) == 0)
                                && (s.substring(len - 4, len).equals(".zip"))) {
                            fList.add(s);
                            int de = getZipIndex(s);
                            if (de == mConfig.mLogFileNum - 1) {
                                new File(mConfig.mLogPathFolder, mConfig.mFileName + "." +
                                        (mConfig.mLogFileNum - 1) + ".zip").delete();
                                continue;
                            }
                            if (de > maxIndex)
                                maxIndex = de;
                        }
                    }

                    while (maxIndex >= 0) {
                        boolean find = false;
                        for (String s : fList) {
                            int de = getZipIndex(s);
                            if (de == maxIndex) {
                                new File(mConfig.mLogPathFolder, mConfig.mFileName + "." + de + ".zip")
                                        .renameTo(new File(mConfig.mLogPathFolder, mConfig.mFileName +
                                                "." + (de + 1) + ".zip"));
                                maxIndex--;
                                find = true;
                                break;
                            }
                        }
                        if (!find) {
                            maxIndex--;
                        }
                    }
                    zipUtil.zipFile(new File(mConfig.mLogPathFolder, mConfig.mFileName).getAbsolutePath(),
                            new File(mConfig.mLogPathFolder, mConfig.mFileName + "." + 0 + ".zip").getAbsolutePath());
                    mfile.delete();
                    mfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private int getZipIndex(String name) {
            int len = name.length();
            return Integer.parseInt(name.substring(len - 5, len - 4));
        }

        public static class Config {
            /**
             * 保存的log文件名称
             */
            public String mFileName = "log.txt";

            /**
             * 保存log的文件夹
             */
            public String mLogPathFolder = "";

            /**
             * 单个文件的最大长度
             */
            public int mLogFileLength = 10 * 1024 * 1024;

            /**
             * 总共保存多少个log文件
             */
            public int mLogFileNum = 10;

            public Config() {

            }

            public Config(Config config) {
                if (config != null) {
                    if (!isEmpty(config.mFileName)) {
                        this.mFileName = config.mFileName;
                    }
                    if (config.mLogPathFolder != null) {
                        this.mLogPathFolder = config.mLogPathFolder;
                    }
                    if (config.mLogFileLength > 0) {
                        this.mLogFileLength = config.mLogFileLength;
                    }
                    if (config.mLogFileNum > 0) {
                        this.mLogFileNum = config.mLogFileNum;
                    }
                }
            }
        }

    }

    public interface IPrinter {

        public String print(int priority, String tag, String msg);
    }

    public static class DefaultPrinter implements IPrinter {
        @Override
        public String print(int priority, String tag, String msg) {
            StringBuilder sb = new StringBuilder();
            sb.append(TIME_LINE.format(new Date(System.currentTimeMillis())));
            sb.append(" ");
            String pid = Utils.getProcessID();

            sb.append(pid);
            sb.append("-");
            sb.append(Thread.currentThread().getId());
            sb.append(" ");
            sb.append(getStringPriority(priority));
            sb.append("/");
            sb.append(tag);
            sb.append(": ");
            sb.append(msg);
            String str = sb.toString();
            System.out.println(str);
            return str;
        }

    }

    public static class Config {

        /**
         * 统一的tag，会替换函数的tag参数，并将tag参数以另外形式打印
         */
        public String tag = null;

        /**
         * 总开关，是否保存log到文件
         */
        public boolean mSaveFile = false;

        /**
         * 总开关是否打印log
         */
        public boolean mPrint = true;


        public Config() {
        }

        public Config(Config config) {
            if (config != null) {
                if (!isEmpty(config.tag)) {
                    this.tag = config.tag;
                }
                this.mSaveFile = config.mSaveFile;
                this.mPrint = config.mPrint;
            }
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    private static String getStringPriority(int priority) {
        switch (priority) {
            case VERBOSE:
                return "V";
            case DEBUG:
                return "D";
            case WARN:
                return "W";
            case INFO:
                return "I";
            case ERROR:
                return "E";
        }
        return "";
    }

}