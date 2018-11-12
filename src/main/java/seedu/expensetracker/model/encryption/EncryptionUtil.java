package seedu.expensetracker.model.encryption;

import static com.google.common.hash.Hashing.murmur3_128;
import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.tag.Tag;

//@@author JasonChong96
/**
 * Utility methods related to data encryption
 */
public class EncryptionUtil {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String DEFAULT_ENCRYPTION_KEY = createEncryptionKey("");

    private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5PADDING";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_LENGTH = 32;

    /**
     * Decrypts a String with by using the input encryption key
     * @param toDecrypt the String to be decrypted
     * @param encryptionKey the encryption key to be used
     * @return the decrypted form of the input String
     * @throws IllegalValueException if the given encryption key is not the encryption key used to encrypt the
     * input String
     */
    public static String decryptString(String toDecrypt, String encryptionKey) throws IllegalValueException {
        requireAllNonNull(toDecrypt, encryptionKey);
        assert encryptionKey.length() == KEY_LENGTH;
        Key key = new SecretKeySpec(encryptionKey.getBytes(), KEY_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(toDecrypt));
            return new String(decryptedBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
            throw new IllegalValueException(String.format("Error decrypting String: %s", toDecrypt), e);
        }
    }

    /**
     * Encrypts a String with the input encryption key. The encryption algorithm uses a symmetric key.
     * i.e the same encryption key is used to decrypt and encrypt a given String.
     * The input encryption key must be 128-bits long.
     * Given the same input string and encryption key, the encrypted output Strings will be equivalent.
     * @param toEncrypt the String to be encrypted
     * @param encryptionKey the encryption key to be used, must be 128-bits long
     * @return the encrypted String
     * @throws IllegalValueException if the given encryption key is not valid. i.e not 128-bits long
     */
    public static String encryptString(String toEncrypt, String encryptionKey) throws IllegalValueException {
        requireAllNonNull(toEncrypt, encryptionKey);
        assert encryptionKey.length() == KEY_LENGTH;
        Key key = new SecretKeySpec(encryptionKey.getBytes(), KEY_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalValueException(String.format("Error encrypting String: %s", toEncrypt), e);
        }
    }

    /**
     * Creates a 128-bit encryption key using the input password String. Equivalent passwords will always map to
     * equivalent keys.
     * @param password the input password String
     * @return the 128-bit encryption key as a String
     */
    public static String createEncryptionKey(String password) {
        requireNonNull(password);
        return murmur3_128().hashString(password, DEFAULT_CHARSET).toString();
    }

    /**
     * Creates an EncryptedExpense instance of the given Expense by encrypting its data using the given encryption key.
     * @param src the Expense to encrypt
     * @param key the encryption key to use
     * @return an EncryptedExpense instance
     * @throws IllegalValueException if the given key is invalid
     */
    public static EncryptedExpense encryptExpense(Expense src, String key) throws IllegalValueException {
        requireNonNull(src, key);
        assert key.length() == KEY_LENGTH;
        Set<EncryptedTag> encryptedTags = new HashSet<>();
        for (Tag tag : src.getTags()) {
            encryptedTags.add(new EncryptedTag(tag, key));
        }
        return new EncryptedExpense(new EncryptedName(src.getName(), key),
                new EncryptedCategory(src.getCategory(), key),
                new EncryptedCost(src.getCost(), key),
                new EncryptedDate(src.getDate(), key),
                encryptedTags);
    }

    /**
     * Creates an EncryptedExpenseTracker instance of the given ExpenseTracker by encrypting it using its encryption
     * key.
     * @param src the ExpenseTracker to encrypt
     * @return an EncryptedExpenseTracker instance
     * @throws IllegalValueException if the ExpenseTracker's encryption key is invalid
     */
    public static EncryptedExpenseTracker encryptTracker(ReadOnlyExpenseTracker src) throws IllegalValueException {
        EncryptedExpenseTracker result = new EncryptedExpenseTracker(src.getUsername(), src.getPassword().orElse(null),
                src.getMaximumTotalBudget(), src.getNotificationHandler());
        for (Expense expense : src.getExpenseList()) {
            result.addExpense(encryptExpense(expense, src.getEncryptionKey()));
        }
        return result;
    }
}
