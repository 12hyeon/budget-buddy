package hyeon.buddy.utility;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/* 비밀번호 유효성 검사를 위한 클래스 : 4가지 조건 */
@UtilityClass
public class Validation {

    /* NordPass 기준, 2022년 빈번하게 사용된 비밀번호 20개 */
    private final List<String> USUAL_PASSWORD_LIST = Arrays.asList(
            "password", "123456", "123456789", "guest", "qwerty",
            "12345678", "111111", "12345", "col123456", "123123",
            "1234567", "1234", "1234567890", "000000", "555555",
            "666666", "123321", "654321", "7777777", "123"
    );

    /* 사용 가능한 문자 형태 3종류 */
    private final List<String> CHARACTER_PATTERN =
            Arrays.asList(".*[a-zA-Z].*", ".*[0-9].*", ".*[!@#$%^&*()].*");

    /* 비밀번호 조건 1 : 자주 사용되는 비밀번호는 사용할 수 없습니다. */
    public boolean isUsualPassword(String password) {
        return USUAL_PASSWORD_LIST.contains(password);
    }

    /* 비밀번호 조건 2 : 비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다. */
    public static boolean containsTwoOrMoreCharacterTypes(String password) {
        int typeCount = 0;

        // 문자, 숫자, 특수문자 중 포함하는 문자 형태의 개수 확인
        for (String pattern : CHARACTER_PATTERN) {
            if (password.matches(pattern)) {
                typeCount++;
            }
        }

        return typeCount < 2;
    }

    /* 비밀번호 조건 3 : 같은 문자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다. */
    public static boolean hasSameCharacters(String password) {
        return IntStream.range(0, password.length() - 3 + 1)
                .anyMatch(i -> password.substring(i, i + 3)
                        .chars()
                        .allMatch(c -> c == password.charAt(i)));
    }

    /* 비밀번호 조건 4 : 연속된 문자 및 숫자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다. */
    public static boolean hasConsecutiveCharacters(String password) {
        return IntStream.range(0, password.length() - 2)
                .anyMatch(i -> {
                    char currentChar = password.charAt(i);
                    char nextChar = password.charAt(i + 1);
                    char secondNextChar = password.charAt(i + 2);

                    // 연속 여부 확인
                    return isSequential(currentChar, nextChar, secondNextChar);
                });
    }

    /* 연속된 문자 또는 문자 여부 확인 */
    private boolean isSequential(char a, char b, char c) {

        // 연속된 문자 여부
        boolean sequence = (a + 1 == b && b + 1 == c) || (a - 1 == b && b - 1 == c);
        if (sequence) {
            return true;
        }

        // 연속된 숫자 여부
        if (Character.isDigit(a) && Character.isDigit(b) && Character.isDigit(c)) {
            return false;
        }

        return false;
    }
}

