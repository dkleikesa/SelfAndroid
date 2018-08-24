package com.qcmian.litto;


import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JFrame;


public class Main {


    static class BoardListener implements ClipboardOwner {
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

        public void start() {
            sysClip.setContents(sysClip.getContents(null), this);
            System.out.println("Listening to board...");
            new JFrame().setVisible(true);
        }

        public void lostOwnership(Clipboard c, Transferable t) {
            Transferable contents = sysClip.getContents(null); //EXCEPTION
            processContents(contents);
            sysClip.setContents(contents, this);
        }

        void processContents(Transferable t) {
            String str = null;
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    str = (String) t.getTransferData(DataFlavor.stringFlavor);
                    System.out.println("Processing: " + str);
                    StringSelection tmp = new StringSelection(str);

                    sysClip.setContents(tmp, this);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                sysClip.setContents(sysClip.getContents(null), this);
            }
        }

        void regainOwnership(Transferable t) {

        }
    }

    public static void main(String[] args) {
        BoardListener b = new BoardListener();
        b.start();
    }

}
