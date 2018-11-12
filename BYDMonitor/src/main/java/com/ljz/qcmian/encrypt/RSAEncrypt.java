package com.ljz.qcmian.encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import com.ljz.qcmian.utils.Utils;

import java.util.List;

import javax.crypto.Cipher;

/**
 * Created by jianzhang.ljz on 2017/10/10.
 */

public class RSAEncrypt {

    public static String dbKey = "012213007";
    public static PublicKey[] publicKeys = {

            getPublicKeyFromStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5qnwkwF5o6Px3jlbD9uCCVh6naDjepq8RgcO2L9S72M+64cZWqbyHU7dT/e6FIEDC4+N3kyyFAWJVSCyfXVBDVZ05tsiQznXY5SqBItkwBP26Q4UQMhz737cQ/SzAI4QCuVybfEiCIBhfFrIGrrckMpL9pl0KuLrHqhriihs5EwIDAQAB"),
            getPublicKeyFromStr("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmiQSO9qAdHal4WqKMZAB58MzwgHgOtKKiazWDZzEtTVlwtsqBYl7HwJsRGhA7TM5Y5TPuAG9A/E4RQrlOGQ0Zvvxi8Ku+HJriIO7VJMhDq1J7WD87k4g1uHboov3zNbEV5H2Dx1aCBGvDlJc6/PlHpg9XSkRIgOdLR0lPuKzkrUPJQD5/6WEA4ADUmdilT+4mpL26uePrynqp/e1NwPugYCt/MfG4yUcQ8aQR+h9KF0fLl9ayVjsMpUaCJ53JCeP+kmfRfJSNkwg3ZbHbxsO+pn8b+AE+Ed7z+ag5LlexXV0kcmMhCqaY5z7yB8eR2AsC9yZbzgeLjaWuMxdWI8kgQIDAQAB"),
            getPublicKeyFromStr("MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAqRA7HYj/2iy7SeB6UcXusATIdJ3EWV0PbkKpVULe6O7Q8LdAZ4uQhvWjX8FLn3PHabzmu8s49V8MLCaRXGlRfkjUcV/fvNBYoi1rk/TNF3Go/WDbUj6sbl/ZaOPavYNrnTX2GcYDcVgAlMXVpOGkraBOXmp7rfGezN8bOgYywusERyMGyItFxdWeVRjcBH8MuvCwGPfk8J1paC3a5tbNnV4nHm6NVtFCuXJIJ08dKaB15ls62uJec44iUbEGsnhY5t0tL7chqEj7cxGCnZGAxQ775iZnAg8afS9AgawD/TUseMw0n7lvG7LuP4XhHb+rFtDIN88SZKTREw2yaf9P62++Xnzb26dd6TYsstU0sRuFJVDym3Lx2hASV6c9TcZ25EZkg7PKs7bv4O1Tlg0eOPel9VZxALoMGgSEoAkWrEvUmN5+ZBPCKPdekSZgFA3IT0AYZagLS2ZuI+0vgj5zd5t+t8sN41tiUe4T/LHwW8Vnh7y7b27b+wqG88V2PjdZAgMBAAE="),
            getPublicKeyFromStr("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEArUZMFilMqt6HzVAco2osNHXpPcXuaarNGw/cfNRTngcSP3WrOB5oAW9d7f/Uk2ITNnnqpHGuwJeQMyAhgwQOdGjSQ+yg1/vKIAO+bb5TxKfzHomTbxUZFl5K5Tqux6smwGcuFlKLgrKH1OBWcDPnnT+wpMew6CNXz44G78rehZqwdkVlqa2FuRnESmD/h4dtIqTdTsSDXEPHcVIesytowF6sSJDKK6Hy4Ly03o/5YlR3QEt/fjG0ogmRnc6HJI8kamTDXB2x2uzJYeBYfxZ1RHGE8mm34DnD/HmNAk8i3URoW6r1ZbQ6tjJiUESIzySc5OzhJQd+Ibe/Fl3HN001J66892ZgkDWlwbydVkoIbaRafuPH5yhYRs9dD0W1OOhu4OuRkxTkbhs6Usv532zVUwcT3dTgOuSOOgYMrjmJrRvrftvDNN7xGjC4BwWXokipfg7v0tglDnAiYRRWCsNuWB0Bq3zd67965q4cpBxPI//y8glFq1oejuV8dbjdXH2r55u8MvNvniZDz6KnN9Y/q/wAyqE3VRliwRhi6VA6WLNp3Zh3dDtWJQ3YVwtiM6uuE2Ob0ijTfplVS8zcTpWbf1cH5VqyHI57fGmUOFPr0IuGQ1MYH2sIYJI5UJCtdIjurvC1jhfDmKev82Refiq+T5hXxfvINApKsh1z9bBc5qMCAwEAAQ==")
    };

    public static PublicKey getPubKeyByLen(int len) {
        if ((len <= 0) || (len > (128 * 4 - 11))) {
            return null;
        }
        int idx = ((len + 11) % 128) == 0 ? (len + 11) / 128 - 1 : (len + 11) / 128;
        return publicKeys[idx];
    }

    public static List<KeyPair> genAndSaveQcmianRSAKey(String fileStr) {
        List<KeyPair> list = new ArrayList<KeyPair>();
        try {
            KeyPair key1024 = genKeyPair(1024);
            KeyPair key2048 = genKeyPair(2048);
            KeyPair key3072 = genKeyPair(3072);
            KeyPair key4096 = genKeyPair(4096);
            list.add(key1024);
            list.add(key2048);
            list.add(key3072);
            list.add(key4096);
            RSAEncrypt.saveKeyPairs(fileStr, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static KeyPair genKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }

    public static void saveKeyPairs(String fileStr, List<KeyPair> keyPairs) {
        String publicFile = fileStr + ".public";
        String privateFile = fileStr + ".private";
        File pbf = new File(publicFile);
        File pvf = new File(privateFile);
        try {
            if (pbf.exists()) {
                pbf.delete();
                pbf.createNewFile();
            }
            if (pvf.exists()) {
                pvf.delete();
                pvf.createNewFile();
            }
            FileOutputStream pbfos = new FileOutputStream(pbf);
            FileOutputStream pvfos = new FileOutputStream(pvf);
            for (KeyPair kp : keyPairs) {
                PublicKey pbk = kp.getPublic();
                String pbkStr = Utils.base64EncodeToString(pbk.getEncoded());
                pbfos.write(pbkStr.getBytes("utf-8"));
                pbfos.write("\n".getBytes("utf-8"));
                PrivateKey pvk = kp.getPrivate();
                String pvkStr = Utils.base64EncodeToString(pvk.getEncoded());
                pvfos.write(pvkStr.getBytes("utf-8"));
                pvfos.write("\n".getBytes("utf-8"));
            }
            pbfos.close();
            pvfos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<KeyPair> readKeyPairs(String fileStr) {
        String publicFile = fileStr + ".public";
        String privateFile = fileStr + ".private";
        File pbf = new File(publicFile);
        File pvf = new File(privateFile);
        List<KeyPair> list = new ArrayList<KeyPair>();
        try {
            if ((!pbf.exists()) || (!pvf.exists())) {
                return null;
            }
            FileInputStream pbfos = new FileInputStream(pbf);
            FileInputStream pvfos = new FileInputStream(pvf);
            BufferedReader pbbr = new BufferedReader(new InputStreamReader(pbfos));
            BufferedReader pvbr = new BufferedReader(new InputStreamReader(pvfos));
            String line;
            while ((line = pbbr.readLine()) != null) {
                PublicKey pbk = getPublicKeyFromStr(line);
                line = pvbr.readLine();
                PrivateKey pvk = getPrivateKeyFromStr(line);
                KeyPair keyPair = new KeyPair(pbk, pvk);
                list.add(keyPair);
            }

            pbbr.close();
            pvbr.close();
            pbfos.close();
            pvfos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static PublicKey getPublicKeyFromStr(String publicKey) {
        try {
            byte[] keyBytes = Utils.base64Decode(publicKey.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKeyFromStr(String privateKey) {
        try {
            byte[] keyBytes = Utils.base64Decode(privateKey.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptWithPadding(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  //java默认"RSA"="RSA/ECB/PKCS1Padding" NoPadding
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptWithPadding(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  //java默认"RSA"="RSA/ECB/PKCS1Padding"NoPadding
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] encryptNoPadding(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");  //java默认"RSA"="RSA/ECB/PKCS1Padding" NoPadding
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptNoPadding(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");  //java默认"RSA"="RSA/ECB/PKCS1Padding"NoPadding
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] encryptWithPadding(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  //java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptWithPadding(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  //java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
