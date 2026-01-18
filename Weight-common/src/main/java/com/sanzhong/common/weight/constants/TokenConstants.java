package com.sanzhong.common.weight.constants;

/**
 * Token相关常量
 *
 * @author visual-ddd
 * @since 1.0
 */
public final class TokenConstants {

    private TokenConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }

    /** JWT Token Claims - 用户ID字段名 */
    public static final String CLAIM_USER_ID = "userId";

    /** JWT Token Claims - 手机号字段名 */
    public static final String CLAIM_PHONE = "phone";

    /** JWT Token Claims - Token类型字段名 */
    public static final String CLAIM_TYPE = "type";

    /** JWT Token类型 - Refresh Token */
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    /** JWT签名算法 */
    public static final String SIGNATURE_ALGORITHM = "HS256";
}
