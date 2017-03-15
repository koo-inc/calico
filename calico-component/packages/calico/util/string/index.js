export function randomString(length, chars) {
    if (chars == null) {
        chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    }
    var clen = chars.length;
    var ret = "";
    for (var i = 0; i < length; i++) {
        ret += chars[Math.floor(Math.random() * clen)];
    }
    return ret;
}
//# sourceMappingURL=index.js.map