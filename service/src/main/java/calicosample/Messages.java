package calicosample;

import java.util.Map;
import java.util.function.Function;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.endpoint.validation.FieldAccessor;

/**
 * このクラスは自動生成されました。
 * 変更する場合はプロパティファイルのほうを修正してください。
 */
public final class Messages {
  
      public static final Message INTERNAL_SERVER_ERROR = new Message("サーバーエラーが発生しました。");
      
      public static final Message NOT_AUTHORIZED = new Message("許可されていない操作です。");
      
      public static final Message EXISTS_ENTITY = new Message("指定されたレコードが存在しません。");
      
      public static Function<FieldAccessor, Message> SCRIPT_ASSERT(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("{script}による評価が不正です。", mapper);
      }
      
      public static final Message SAFE_HTML = new Message("安全ではないHTMLが含まれています。");
      
      public static final Message REQUIRED = new Message("必須項目です。");
      
      public static final Message NOT_BLANK = new Message("ブランクは許可されていません。");
      
      public static final Message NOT_EMPTY = new Message("何らかのデータが必要です。");
      
      public static Function<FieldAccessor, Message> UPPER_BOUND(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("{value}以下でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> LOWER_BOUND(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("{value}以上でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> RANGE(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("{min}から{max}の範囲内でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> DIGITS(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("境界以外の数値（予測:<{integer}digits>.<{fraction}digits>）", mapper);
      }
      
      public static Function<FieldAccessor, Message> LETTER_COUNT(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("{lowerBound}文字以上{upperBound}文字以下でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> PATTERN(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("パターン（{regexp}）に一致しなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> LENGTH(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("長さは{min}以上{max}以下でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> SIZE(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("サイズは{lowerBound}以上{upperBound}以下でなければなりません。", mapper);
      }
      
      public static final Message FUTURE = new Message("未来日付でなければなりません。");
      
      public static final Message PAST = new Message("過去日付でなければなりません。");
      
      public static final Message FROM_TO_DATE = new Message("終了日は開始日より後の日付を指定してください。");
      
      public static final Message FROM_TO_TIME = new Message("終了時間は開始時間より後の時間を指定してください。");
      
      public static final Message FROM_TO_TIMESTAMP = new Message("終了日時は開始日時より後の日時を指定してください。");
      
      public static Function<FieldAccessor, Message> FILE_SIZE(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("ファイルサイズは{min}以上{max}以下でなければなりません。", mapper);
      }
      
      public static Function<FieldAccessor, Message> ALLOWED_EXTENSIONS(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("拡張子は{value}のうちのいずれかでなければなりません。", mapper);
      }
      
      public static final Message INTEGER_FORMAT = new Message("正しい数値の形式ではありません。");
      
      public static final Message FLOAT_FORMAT = new Message("正しい数値の形式ではありません。");
      
      public static final Message DATE_FORMAT = new Message("正しい日付の形式ではありません。");
      
      public static final Message TIME_FORMAT = new Message("正しい時間の形式ではありません。");
      
      public static final Message PHONE_NUMBER_FORMAT = new Message("正しい電話番号の形式ではありません。");
      
      public static final Message EMAIL_FORMAT = new Message("正しいメールアドレスの形式ではありません。");
      
      public static final Message URL_FORMAT = new Message("正しいURLの形式ではありません。");
      
      public static final Message CREDIT_CARD_NUMBER_FORMAT = new Message("正しいクレジットカード番号の形式ではありません。");
      
      public static final Message UNIQUE_LOGIN_ID = new Message("既に使われているIDです。");
      
}
