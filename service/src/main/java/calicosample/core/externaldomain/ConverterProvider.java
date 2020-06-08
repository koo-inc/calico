package calicosample.core.externaldomain;

import org.seasar.doma.DomainConverters;

/**
 * Created by tasuku on 15/05/08.
 */
@DomainConverters({MediaConverter.class, TimePointConverter.class})
public class ConverterProvider {
}
