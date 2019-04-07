package sample.bean;


import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;

/**
 * traced tx
 */
@Stateless
@Traced
public class AccountService {
    public void onNewOrder() {
        // we would do something interesting here
    }
}
