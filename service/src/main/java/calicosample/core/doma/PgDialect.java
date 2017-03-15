package calicosample.core.doma;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;
import jp.co.freemind.calico.jackson.JsonObject;
import lombok.Getter;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.jdbc.type.PortableObjectType;
import org.seasar.doma.wrapper.StringWrapper;

/**
 * Created by kakusuke on 15/07/30.
 */
public class PgDialect extends PostgresDialect {

  public PgDialect() {
    super(new PgJdbcMappingVisitor());
  }

  @Getter
  private final ExpressionFunctions expressionFunctions = new PgExpressionFunctions();

  public class PgExpressionFunctions extends StandardExpressionFunctions {
    public PgExpressionFunctions() {
      super('\\', new char[] {'_', '%'});
    }

    public boolean isPresent(Object obj){
      if(obj == null) return false;
      Object val = obj;
      if(val instanceof Optional){
        Optional op = (Optional) val;
        val = op.isPresent() ? op.get() : null;
      }
      if(val == null) return false;
      if(val instanceof String && Strings.isNullOrEmpty((String) val)) return false;
      if(val instanceof List && ((List) val).size() == 0) return false;
      return true;
    }
  }

  private static class PgJdbcMappingVisitor extends PostgresJdbcMappingVisitor {
    @Override
    public Void visitStringWrapper(StringWrapper wrapper, JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
      if (q.getDomainClass().map(JsonObject.class::isAssignableFrom).orElse(false)) {
        return p.apply(wrapper, getPortableObject(JdbcTypes.STRING));
      }
      return super.visitStringWrapper(wrapper, p, q);
    }

    private static ConcurrentHashMap<JdbcType<?>, PortableObjectType<?>> cache = new ConcurrentHashMap<>();
    @SuppressWarnings("unchecked")
    private <T> PortableObjectType<T> getPortableObject(JdbcType<?> jdbcType) {
      return (PortableObjectType<T>) cache.computeIfAbsent(jdbcType, PortableObjectType::new);
    }
  }
}
