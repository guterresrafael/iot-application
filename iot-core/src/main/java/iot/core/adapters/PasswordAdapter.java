package iot.core.adapters;

import java.math.BigInteger;
import java.security.MessageDigest;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Rafael Guterres
 */
public class PasswordAdapter extends XmlAdapter<String, String> {

    private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";
    private static final String STRING_FORMAT = "%32x";
    private static final int SIGNUM_POSITIVE = 1;

    @Override
    public String marshal(String value) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        BigInteger hash = new BigInteger(SIGNUM_POSITIVE, messageDigest.digest(value.getBytes()));
        return String.format(STRING_FORMAT, hash);
    }

    @Override
    public String unmarshal(String value) throws Exception {
        return value;
    }
}
