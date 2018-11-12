package com.ljz.qcmian;

import android.util.Log;

import com.ljz.qcmian.encrypt.RSAEncrypt;
import com.ljz.qcmian.utils.LLog;
import com.ljz.qcmian.utils.LogFactory;
import com.ljz.qcmian.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Hello {

    public static final boolean APPpp = false;

    static String TAG = "hello";
    static LLog llog = LogFactory.getLLog();

    public static void main(String[] args) {
//        PublicKey pk = RSAEncrypt.getPublicKeyFromStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5qnwkwF5o6Px3jlbD9uCCVh6naDjepq8RgcO2L9S72M+64cZWqbyHU7dT/e6FIEDC4+N3kyyFAWJVSCyfXVBDVZ05tsiQznXY5SqBItkwBP26Q4UQMhz737cQ/SzAI4QCuVybfEiCIBhfFrIGrrckMpL9pl0KuLrHqhriihs5EwIDAQABJQD5/6WEA4ADUmdilT+4mpL26uePrynqp/e1NwPugYCt/MfG4yUcQ8aQR+h9KF0fLl9ayVjsMpUaCJ53JCeP+kmfRfJSNkwg3ZbHbxsO+pn8b+AE+Ed7z+ag5LlexXV0kcmMhCqaY5z7yB8eR2AsC9yZbzgeLjaWuMxdWI8kgQIDAQAB");
//        Log.e("TAG", pk.toString());
//        pk = RSAEncrypt.getPublicKeyFromStr("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmiQSO9qAdHal4WqKMZAB58MzwgHgOtKKiazWDZzEtTVlwtsqBYl7HwJsRGhA7TM5Y5TPuAG9A/E4RQrlOGQ0Zvvxi8Ku+HJriIO7VJMhDq1J7WD87k4g1uHboov3zNbEV5H2Dx1aCBGvDlJc6/PlHpg9XSkRIgOdLR0lPuKzkrUP");
//        Log.e("TAGG", pk.toString());
//
//        PublicKey pk = RSAEncrypt.getPublicKeyFromStr("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1SowfZh3tlp9julNi1jiN5YsdCQmTCt7reQrwQ8tkCLFTcYTn60jEWInGjuDnJ6ryhzAn0ShDees70MRCBgMEWNpm7s1aeIoRNX483Edfm6us+KAUkR4Q19jELhfD0IFs9L57PiVsCTKTNNgi9F6nVRJ2IgeknDN0yJyPJpYYapPJbRXJD0O55aXzHpLEnvDrzkHsIvdeLmlmrhMaPvWPChCFv/0n0zE/PvARCScpZ2x+ciK2986UYKHGNuixsFufZe0q+tKUNdNDtigYN6iaigyQYRjCKR0xhLG0XAd6nkOQZ4WiVVPJ6tT8j0JRbjpcYEHVtEyJi4Pb6JyHeKwjwIDAQAB");
//        Log.e("TAGG", pk.toString());

        String RSA_PRIVATE_TEST = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCm95R12lb4Hu4Lk0sTJtdGxXJ63fgDo1bMztA80fkHprsp87G2WZ4Rv/kK0kFMzFdbxmWV8+Er2sQZ7SAVdmwhjW6EtlTJAzRT59FFXQ7ffVlQaunxx6p/6PnijOtGbilo96z/8n2R1gce0s09XHRbH9tvWraMkpAhUCHCyf+0ZYHIWZgFZfab3qog0Mcw6BAeTpDdql6hqEmVHsyBFy8yxcHoGAFI/94UB/xvqQiSMG2jAYxSu9CjOsh0/oyWFbEqcHgTxHX7pIRUrN0rf6zqtUsLQf+UaRKE0Uq3Zn19D+IRLOBlZ/vLRqb2eNyeYeAbVgMbW4eI+cuzkuNqISnxAgMBAAECggEAJV/aHZ9oRFY4FuM7tOfG3JKqE5LIR5gyf9nzhwnBYtMRpkxkhVr+JR8B0khKUbSUAXkhmDVlO/nWV69atTMy9TfBe3eM9wn+lqGXmJ1CDQj0CypDf9mf8s3l0a6Vo8hZAKQgV5KiRRjRszagtpGRgixZZE27+y97j9luFTo+QNTzQi4fRvPVc4RNDFKYcz4QWxNcIOEBi7xKMgSGktPGqcsZjvnGH157tv479Grx+TZxEIbat/SLRjD0S2NrLgDx+CWCkv/AMTJvoxCchUpLWwZi5QnLiOV9TXRqGn+RUF2ZbnV0uUKpG1cuMERHyqYFEpw8qK5tsXwCwlbqO4DpgQKBgQDb5eznaIXkQsd5moYX9DMtxTLzefC/zIcm5ugcc+ecB8xpwuUQgKW1lf2k8sdxZZoV65FWHq+hKV2wgBbMrOkUV9Im0nXK6YYBqOiZtCP3n07qwOEE5FFmKkO3DJ8iBwKtIbJ8kceotqZyNnDMqKApPUHvjqd5mMOW5fQyqtKeSQKBgQDCYQWZ8XTQOL3VLWeapfWiAV8Ox3zZoMfTGbtEUqr+iAZN0DEAd9wKl1g5fxB0uVYeouIJntK3eReCeBGfAlX4jw8SCkwic3uB6W7DT5dynDxJZ8nY5DeI17+ZD5Q6+CWtEHG2vUPvTFKy5H65qwlfC5ER9oohsaOd/TsSkWROaQKBgCUR1aYNEMUyHL57Ni/Dkv0cSUKSQ+uRZxc/xdFGGL1M80DBAiyOA2FhL6km5EhRgHBBjfaepazddFXUwgMvAvvS8jJpOEJEq7qL5upCW+3ahUs9yLEybCZ06YVqM0lhNSpKi/RD/wyJ/fUzCED4DEfnc74WplTxU8eUbF4+PdNBAoGAXh/LLZNdhGKlke/tplZMzokpdaelzmBrws5H/zqksKI/ozh4MgjYVYyZ3SWpW0xP5n/rQstUsCGD/9qSddQUu0rS+mJgIaKYIP1fdFY7OPVswALxHATO24XVspF3ruJwpBA9cEbP+bWUqim5L8EhxZN9SRyAIPa7CwsPqtsanlkCgYAqgcbPu0cvszFa0m62lTotw7qE8puBPPBuxM2EClwBbWiHxs7nFRH+c96+P/W2q8LK/AW1Wluus97YlSozL50uSkZAxtbWDSilZpXq9kYbK9zWS5Ngn+H6rpFfY5ANp0WCM7fw2JzwKvHGUxt59EmFLelTYwu/hxIY4uILWdIGGw==";
        //正式秘钥
        String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMguYBEHR99slsyl\n" +
                "V9xtPyDueJsro5yhRdIYBCE2OvQMfuA2b/nvZlrdUmuOcv+bXN3ujv5pZb9PB2jF\n" +
                "700q3i5F/qqhiLYl0xtzejg2D8e445zoAHuDT2tL1kjNDmXC4dxFFGZZ+yMxzCnO\n" +
                "y2Y/twryuGJF0n66c3chw+Oi4ejlAgMBAAECgYBT8KZV3aC0vlsJmzeZdbHoBDdM\n" +
                "keL8dd/KNkndB1l3Jpo5OHqB6nIYHgBGm6f7KNGrOjJ52gZRTzlDJOSwjg41yTWg\n" +
                "26NmUHrlukrsHCv2ndoeJGBh6X9RZRkJxgGXWZ0NOt6badtRhoOoCe7DqTX94ZBQ\n" +
                "AFkcYxP8p8n17IdoAQJBAOivgYsPErzX41M3O+QnSpzNp5jzfr3qG7pZdUdzlA4Y\n" +
                "5f0oJpwe+kGTcPgcXiD3kpHX+HmlYDYQmU4tAjoPRuUCQQDcPR2eLmzZpC6tpYyp\n" +
                "xYLI2qCw9hsTWvGSHfVH7FrKpRId0XR0Mf+6YEoLJo3AZ0xDpIcd31Pksimqk6HO\n" +
                "y/oBAkEAj2847M7C3zQ5xp9ixPbPkK9ZY/idpVZ99zaUDBKcLsB8bbzlaBHUdL39\n" +
                "woRCJhJXAJ5gZiRilZFP35fxKncmXQJAIIQ1d0FLeOawrZqfpgEvShBdYUM0xCrN\n" +
                "N9GMgU34KastfZGLLAylwRKuW+8ZRqr5q5MDD/oFHOLhG/ooDaw4AQJBAMtZD/dU\n" +
                "7abQL5iSXzjQPVdNl5XxHSi4bGvPRycrbdrlV7IN0/J4aDih2ok/jexh+2SB8t82\n" +
                "AhV3eKzeM+GQzM8=\n";

        PrivateKey pk = RSAEncrypt.getPrivateKeyFromStr(RSA_PRIVATE_TEST);
        Log.e("TAGG", pk.toString());

        pk = RSAEncrypt.getPrivateKeyFromStr(RSA_PRIVATE);
        Log.e("TAGG", pk.toString());

        if (true) {
            return;
        }

        String url = "http://www.qcmian.com/?action=send_data";
        String parm = null;
        try {

            StringBuilder sb = new StringBuilder();
            sb.append('0');
            for (int i = 0; i < 50; i++) {
                sb.append('1');
            }
            sb.append('0');
            parm = buildParam("ljz", sb.toString().getBytes("utf-8"));

            String ret = Http.sendPost(url, parm);
            llog.d(TAG, "ret:" + ret);
            String data = ret.substring(ret.indexOf("datass="), ret.indexOf("\nfinish"));
            data = data.replace("datass=", "");
            byte[] dd = Utils.base64Decode(data.getBytes("utf-8"));
            dd = RSAEncrypt.decryptWithPadding(dd, RSAEncrypt.getPubKeyByLen(sb.length()));
            data = new String(dd, "utf-8");
            llog.d(TAG, "ret:" + data);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static String buildParam(String id, byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(id);
        sb.append("&sz=");
        sb.append(data.length);
        sb.append("&data=");
        PublicKey kp = RSAEncrypt.getPubKeyByLen(data.length);
        data = RSAEncrypt.encryptWithPadding(data, kp);
        data = Utils.base64Encode(data);
        String str = null;
        try {
            str = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append(str);
        return sb.toString();

    }


    public static void genKey() {
        try {
            List<KeyPair> list = RSAEncrypt.genAndSaveQcmianRSAKey("d:\\debug\\keys");
            for (KeyPair kp : list) {
                llog.d(TAG, kp.getPublic().toString());
            }


            List<KeyPair> list1 = RSAEncrypt.readKeyPairs("d:\\debug\\keys");

            for (KeyPair kp : list1) {
                llog.d(TAG, kp.getPublic().toString());
            }
            int size = list1.size();
            for (int i = 0; i < size; i++) {
                llog.d(TAG, "pbk eql:" + list.get(i).getPublic().toString().equals(list1.get(i).getPublic().toString()));
            }
            String tt = "12345";
            byte[] buf = RSAEncrypt.encryptWithPadding(tt.getBytes(), list.get(0).getPrivate());
            buf = RSAEncrypt.decryptWithPadding(buf, list1.get(0).getPublic());
            String str = new String(buf);
            llog.d(TAG, "test:" + str.equals(tt));

            buf = RSAEncrypt.encryptWithPadding(tt.getBytes(), list.get(0).getPublic());
            buf = RSAEncrypt.decryptWithPadding(buf, list1.get(0).getPrivate());
            str = new String(buf);
            llog.d(TAG, "test:" + str.equals(tt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
