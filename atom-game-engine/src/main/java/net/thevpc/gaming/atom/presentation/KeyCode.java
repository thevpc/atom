package net.thevpc.gaming.atom.presentation;

import java.util.*;
import java.util.stream.Collectors;

public enum KeyCode {
    ENTER(0x0A, KeyCodeClass.WHITESPACE),
    BACK_SPACE(0x08),
    TAB(0x09, KeyCodeClass.WHITESPACE),
    CANCEL(0x03),
    CLEAR(0x0C),
    SHIFT(0x10, KeyCodeClass.MODIFIER),
    CONTROL(0x11, KeyCodeClass.MODIFIER),
    ALT(0x12, KeyCodeClass.MODIFIER),
    PAUSE(0x13),
    CAPS(0x14),
    ESCAPE(0x1B),
    SPACE(0x20, KeyCodeClass.WHITESPACE),
    PAGE_UP(0x21, KeyCodeClass.NAVIGATION),
    PAGE_DOWN(0x22, KeyCodeClass.NAVIGATION),
    END(0x23, KeyCodeClass.NAVIGATION),
    HOME(0x24, KeyCodeClass.NAVIGATION),
    LEFT(0x25, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION),
    UP(0x26, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION),
    RIGHT(0x27, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION),
    DOWN(0x28, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION),
    COMMA(0x2C),
    MINUS(0x2D),
    PERIOD(0x2E),
    SLASH(0x2F),
    DIGIT0(0x30, KeyCodeClass.DIGIT),
    DIGIT1(0x31, KeyCodeClass.DIGIT),
    DIGIT2(0x32, KeyCodeClass.DIGIT),
    DIGIT3(0x33, KeyCodeClass.DIGIT),
    DIGIT4(0x34, KeyCodeClass.DIGIT),
    DIGIT5(0x35, KeyCodeClass.DIGIT),
    DIGIT6(0x36, KeyCodeClass.DIGIT),
    DIGIT7(0x37, KeyCodeClass.DIGIT),
    DIGIT8(0x38, KeyCodeClass.DIGIT),
    DIGIT9(0x39, KeyCodeClass.DIGIT),
    SEMICOLON(0x3B),
    EQUALS(0x3D),
    A(0x41, KeyCodeClass.LETTER),
    B(0x42, KeyCodeClass.LETTER),
    C(0x43, KeyCodeClass.LETTER),
    D(0x44, KeyCodeClass.LETTER),
    E(0x45, KeyCodeClass.LETTER),
    F(0x46, KeyCodeClass.LETTER),
    G(0x47, KeyCodeClass.LETTER),
    H(0x48, KeyCodeClass.LETTER),
    I(0x49, KeyCodeClass.LETTER),
    J(0x4A, KeyCodeClass.LETTER),
    K(0x4B, KeyCodeClass.LETTER),
    L(0x4C, KeyCodeClass.LETTER),
    M(0x4D, KeyCodeClass.LETTER),
    N(0x4E, KeyCodeClass.LETTER),
    O(0x4F, KeyCodeClass.LETTER),
    P(0x50, KeyCodeClass.LETTER),
    Q(0x51, KeyCodeClass.LETTER),
    R(0x52, KeyCodeClass.LETTER),
    S(0x53, KeyCodeClass.LETTER),
    T(0x54, KeyCodeClass.LETTER),
    U(0x55, KeyCodeClass.LETTER),
    V(0x56, KeyCodeClass.LETTER),
    W(0x57, KeyCodeClass.LETTER),
    X(0x58, KeyCodeClass.LETTER),
    Y(0x59, KeyCodeClass.LETTER),
    Z(0x5A, KeyCodeClass.LETTER),
    OPEN_BRACKET(0x5B),
    BACK_SLASH(0x5C),
    CLOSE_BRACKET(0x5D),
    NUMPAD0(0x60, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD1(0x61, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD2(0x62, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD3(0x63, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD4(0x64, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD5(0x65, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD6(0x66, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD7(0x67, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD8(0x68, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    NUMPAD9(0x69, KeyCodeClass.DIGIT | KeyCodeClass.KEYPAD),
    MULTIPLY(0x6A),
    ADD(0x6B),
    SEPARATOR(0x6C),
    SUBTRACT(0x6D),
    DECIMAL(0x6E),
    DIVIDE(0x6F),
    DELETE(0x7F), /* ASCII:Integer   DEL */
    NUM_LOCK(0x90),
    SCROLL_LOCK(0x91),
    F1(0x70, KeyCodeClass.FUNCTION),
    F2(0x71, KeyCodeClass.FUNCTION),
    F3(0x72, KeyCodeClass.FUNCTION),
    F4(0x73, KeyCodeClass.FUNCTION),
    F5(0x74, KeyCodeClass.FUNCTION),
    F6(0x75, KeyCodeClass.FUNCTION),
    F7(0x76, KeyCodeClass.FUNCTION),
    F8(0x77, KeyCodeClass.FUNCTION),
    F9(0x78, KeyCodeClass.FUNCTION),
    F10(0x79, KeyCodeClass.FUNCTION),
    F11(0x7A, KeyCodeClass.FUNCTION),
    F12(0x7B, KeyCodeClass.FUNCTION),
    F13(0xF000, KeyCodeClass.FUNCTION),
    F14(0xF001, KeyCodeClass.FUNCTION),
    F15(0xF002, KeyCodeClass.FUNCTION),
    F16(0xF003, KeyCodeClass.FUNCTION),
    F17(0xF004, KeyCodeClass.FUNCTION),
    F18(0xF005, KeyCodeClass.FUNCTION),
    F19(0xF006, KeyCodeClass.FUNCTION),
    F20(0xF007, KeyCodeClass.FUNCTION),
    F21(0xF008, KeyCodeClass.FUNCTION),
    F22(0xF009, KeyCodeClass.FUNCTION),
    F23(0xF00A, KeyCodeClass.FUNCTION),
    F24(0xF00B, KeyCodeClass.FUNCTION),
    PRINTSCREEN(0x9A),
    INSERT(0x9B),
    HELP(0x9C),
    META(0x9D, KeyCodeClass.MODIFIER),
    BACK_QUOTE(0xC0),
    QUOTE(0xDE),
    KP_UP(0xE0, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION | KeyCodeClass.KEYPAD),
    KP_DOWN(0xE1, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION | KeyCodeClass.KEYPAD),
    KP_LEFT(0xE2, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION | KeyCodeClass.KEYPAD),
    KP_RIGHT(0xE3, KeyCodeClass.ARROW | KeyCodeClass.NAVIGATION | KeyCodeClass.KEYPAD),
    DEAD_GRAVE(0x80),
    DEAD_ACUTE(0x81),
    DEAD_CIRCUMFLEX(0x82),
    DEAD_TILDE(0x83),
    DEAD_MACRON(0x84),
    DEAD_BREVE(0x85),
    DEAD_ABOVEDOT(0x86),
    DEAD_DIAERESIS(0x87),
    DEAD_ABOVERING(0x88),
    DEAD_DOUBLEACUTE(0x89),
    DEAD_CARON(0x8a),
    DEAD_CEDILLA(0x8b),
    DEAD_OGONEK(0x8c),
    DEAD_IOTA(0x8d),
    DEAD_VOICED_SOUND(0x8e),
    DEAD_SEMIVOICED_SOUND(0x8f),
    AMPERSAND(0x96),
    ASTERISK(0x97),
    QUOTEDBL(0x98),
    LESS(0x99),
    GREATER(0xa0),
    BRACELEFT(0xa1),
    BRACERIGHT(0xa2),
    AT(0x0200),
    COLON(0x0201),
    CIRCUMFLEX(0x0202),
    DOLLAR(0x0203),
    EURO_SIGN(0x0204),
    EXCLAMATION_MARK(0x0205),
    INVERTED_EXCLAMATION_MARK(0x0206),
    LEFT_PARENTHESIS(0x0207),
    NUMBER_SIGN(0x0208),
    PLUS(0x0209),
    RIGHT_PARENTHESIS(0x020A),
    UNDERSCORE(0x020B),
    WINDOWS(0x020C, KeyCodeClass.MODIFIER),
    CONTEXT_MENU(0x020D),
    FINAL(0x0018),
    CONVERT(0x001C),
    NONCONVERT(0x001D),
    ACCEPT(0x001E),
    MODECHANGE(0x001F),
    KANA(0x0015),
    KANJI(0x0019),
    ALPHANUMERIC(0x00F0),
    KATAKANA(0x00F1),
    HIRAGANA(0x00F2),
    FULL_WIDTH(0x00F3),
    HALF_WIDTH(0x00F4),
    ROMAN_CHARACTERS(0x00F5),
    ALL_CANDIDATES(0x0100),
    PREVIOUS_CANDIDATE(0x0101),
    CODE_INPUT(0x0102),
    JAPANESE_KATAKANA(0x0103),
    JAPANESE_HIRAGANA(0x0104),
    JAPANESE_ROMAN(0x0105),
    KANA_LOCK(0x0106),
    INPUT_METHOD_ON_OFF(0x0107),
    CUT(0xFFD1, KeyCodeClass.CLIPBOARD),
    COPY(0xFFCD, KeyCodeClass.CLIPBOARD),
    PASTE(0xFFCF, KeyCodeClass.CLIPBOARD),
    UNDO(0xFFCB, KeyCodeClass.CLIPBOARD),
    AGAIN(0xFFC9, KeyCodeClass.CLIPBOARD),
    FIND(0xFFD0),
    PROPS(0xFFCA),
    STOP(0xFFC8),
    COMPOSE(0xFF20),
    ALT_GRAPH(0xFF7E, KeyCodeClass.MODIFIER),
    BEGIN(0xFF58),
    UNDEFINED(0x0),
    //
    SOFTKEY_0(0x1000, KeyCodeClass.MOBILE),
    SOFTKEY_1(0x1001, KeyCodeClass.MOBILE),
    SOFTKEY_2(0x1002, KeyCodeClass.MOBILE),
    SOFTKEY_3(0x1003, KeyCodeClass.MOBILE),
    SOFTKEY_4(0x1004, KeyCodeClass.MOBILE),
    SOFTKEY_5(0x1005, KeyCodeClass.MOBILE),
    SOFTKEY_6(0x1006, KeyCodeClass.MOBILE),
    SOFTKEY_7(0x1007, KeyCodeClass.MOBILE),
    SOFTKEY_8(0x1008, KeyCodeClass.MOBILE),
    SOFTKEY_9(0x1009, KeyCodeClass.MOBILE),
    GAME_A(0x100A, KeyCodeClass.MOBILE),
    GAME_B(0x100B, KeyCodeClass.MOBILE),
    GAME_C(0x100C, KeyCodeClass.MOBILE),
    GAME_D(0x100D, KeyCodeClass.MOBILE),
    STAR(0x100E, KeyCodeClass.MOBILE),
    POUND(0x100F, KeyCodeClass.MOBILE),
    POWER(0x199, KeyCodeClass.MOBILE),
    INFO(0x1C9, KeyCodeClass.MOBILE),
    COLORED_KEY_0(0x193, KeyCodeClass.MOBILE),
    COLORED_KEY_1(0x194, KeyCodeClass.MOBILE),
    COLORED_KEY_2(0x195, KeyCodeClass.MOBILE),
    COLORED_KEY_3(0x196, KeyCodeClass.MOBILE),
    EJECT_TOGGLE(0x19E, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    PLAY(0x19F, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    RECORD(0x1A0, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    FAST_FWD(0x1A1, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    REWIND(0x19C, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    TRACK_PREV(0x1A8, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    TRACK_NEXT(0x1A9, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    CHANNEL_UP(0x1AB, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    CHANNEL_DOWN(0x1AC, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    VOLUME_UP(0x1bf, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    VOLUME_DOWN(0x1C0, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    MUTE(0x1C1, KeyCodeClass.MEDIA | KeyCodeClass.MOBILE),
    COMMAND(0x300, KeyCodeClass.MODIFIER | KeyCodeClass.MOBILE),
    SHORTCUT(-1, KeyCodeClass.MOBILE),
    UNKNOWN(-2);

    private final static Map<Integer, KeyCode> byCode = new HashMap<>();

    static {
        for (KeyCode value : KeyCode.values()) {
            byCode.put(value.code, value);
        }
    }

    private final int code;
    private final String chr;
    private final int codeType;

    KeyCode(int code, int codeType) {
        this.code = code;
        this.codeType = codeType;
        chr = String.valueOf((char) code);
    }

    KeyCode(int code) {
        this(code, 0);
    }

    public static KeyCode of(int code) {
        KeyCode a = byCode.get(code);
        return a == null ? UNKNOWN : a;
    }

    static Set<KeyCode> setOf(Set<KeyCode>... o) {
        Set<KeyCode> a = new HashSet<>();
        for (Set<KeyCode> k : o) {
            a.addAll(k);
        }
        return a;
    }

    static Set<KeyCode> setOf(KeyCode... o) {
        if (o == null) {
            return new HashSet<>();
        }
        return (Arrays.stream(o).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    static Set<KeyCode> setOf(KeyCode[] o, KeyCode defaultValue) {
        if (defaultValue == null) {
            return setOf(o);
        }
        if (o == null) {
            return new HashSet<>(Collections.singletonList(defaultValue));
        }
        Set<KeyCode> s = Arrays.stream(o).filter(Objects::nonNull).collect(Collectors.toSet());
        if (s.isEmpty()) {
            s.add(defaultValue);
        }
        return s;
    }

    public int code() {
        return code;
    }

    public String chr() {
        return chr;
    }

    public int codeType() {
        return codeType;
    }

    public final boolean isFunctionKey() {
        return (codeType & KeyCodeClass.FUNCTION) != 0;
    }

    public final boolean isNavigationKey() {
        return (codeType & KeyCodeClass.NAVIGATION) != 0;
    }

    public final boolean isArrowKey() {
        return (codeType & KeyCodeClass.ARROW) != 0;
    }

    public final boolean isModifierKey() {
        return (codeType & KeyCodeClass.MODIFIER) != 0;
    }

    public final boolean isLetterKey() {
        return (codeType & KeyCodeClass.LETTER) != 0;
    }

    public final boolean isDigitKey() {
        return (codeType & KeyCodeClass.DIGIT) != 0;
    }

    public final boolean isKeypadKey() {
        return (codeType & KeyCodeClass.KEYPAD) != 0;
    }

    public final boolean isWhitespaceKey() {
        return (codeType & KeyCodeClass.WHITESPACE) != 0;
    }

    public final boolean isMediaKey() {
        return (codeType & KeyCodeClass.MEDIA) != 0;
    }

    public final boolean isMobile() {
        return (codeType & KeyCodeClass.MOBILE) != 0;
    }

    public final boolean isClipboard() {
        return (codeType & KeyCodeClass.CLIPBOARD) != 0;
    }

    public KeyCodeSet plus(KeyCode... c) {
        return KeyCodeSet.of(this).plus(c);
    }

    public KeyCodeSet plus(Collection<KeyCode> c) {
        return KeyCodeSet.of(this).plus(c);
    }

    public KeyCodeSet plus(KeyCodeSet c) {
        return KeyCodeSet.of(this).plus(c);
    }

    private static class KeyCodeClass {

        private static final int FUNCTION = 1;

        private static final int NAVIGATION = 2;
        private static final int ARROW = 4;
        private static final int MODIFIER = 8;
        private static final int LETTER = 16;
        private static final int DIGIT = 32;
        private static final int KEYPAD = 64;
        private static final int WHITESPACE = 128;
        private static final int MEDIA = 256;
        private static final int MOBILE = 512;
        private static final int CLIPBOARD = 1024;

        private KeyCodeClass() {
        }
    }

}
