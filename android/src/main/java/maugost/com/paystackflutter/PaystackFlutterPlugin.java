package maugost.com.paystackflutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import maugost.com.paystackflutter.creditcarddesign.CardEditActivity;
import maugost.com.paystackflutter.creditcarddesign.CreditCardUtils;

import static android.app.Activity.RESULT_OK;
import static maugost.com.paystackflutter.creditcarddesign.CreditCardUtils.EMAIL;


/**
 * PaystackFlutterPlugin
 */
public class PaystackFlutterPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {

    private static final int PAYSTACK_REQUEST_CODE = 1000;
    private Activity activity;
    private Context context;
    private Result pendingResult;
    private MethodCall methodCall;


    //
    private PaystackFlutterPlugin(PluginRegistry.Registrar registrar) {
        //this.registrar = registrar;
        this.activity = registrar.activity();
        this.context = registrar.context();

    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "maugost.com/paystack_flutter");
        PaystackFlutterPlugin instance = new PaystackFlutterPlugin(registrar);
        registrar.addActivityResultListener(instance);
        channel.setMethodCallHandler(instance);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

       /* if (paystack_public_key.isEmpty()) {
            result.error("PAYSTACK ERROR", "Paystack needs your public key", null);
            return;
        }*/

        this.pendingResult = result;
        this.methodCall = call;


        if (call.method.equals("getPlatformVersion")) {

            if (!methodCall.hasArgument("PAYSTACK_PUBLIC_KEY")) {
                result.error("PAYSTACK ERROR", "Paystack needs your public key", null);
                return;
            }

            if (!methodCall.hasArgument("BACKEND_URL")) {
                result.error("PAYSTACK ERROR", "Paystack needs your backend url", null);
                return;
            }

            if (!methodCall.hasArgument("NAME")) {
                result.error("PAYSTACK ERROR", "Paystack needs your name", null);
                return;
            }

            if (!methodCall.hasArgument("EMAIL")) {
                result.error("PAYSTACK ERROR", "Paystack needs your email", null);
                return;
            }

            if (!methodCall.hasArgument("AMOUNT")) {
                result.error("PAYSTACK ERROR", "Paystack needs your amount", null);
                return;
            }

            if (!methodCall.hasArgument("CURRENCY")) {
                result.error("PAYSTACK ERROR", "Paystack needs your currency", null);
                return;
            }

            if (!methodCall.hasArgument("PAYMENT_FOR")) {
                result.error("PAYSTACK ERROR", "Paystack needs your payment for", null);
                return;
            }

            String paystack_public_key = null;
            if (methodCall.hasArgument("PAYSTACK_PUBLIC_KEY")) {
                paystack_public_key = methodCall.argument("PAYSTACK_PUBLIC_KEY").toString();
                //Toast.makeText(activity, currency, Toast.LENGTH_SHORT).show();
            }

            String backend_url = null;
            if (methodCall.hasArgument("BACKEND_URL")) {
                backend_url = methodCall.argument("BACKEND_URL").toString();
                //Toast.makeText(activity, paymentfor, Toast.LENGTH_SHORT).show();
            }

            String name = null;
            if (methodCall.hasArgument("NAME")) {
                name = methodCall.argument("NAME").toString();
                //Toast.makeText(activity, name, Toast.LENGTH_SHORT).show();
            }

            String email = null;
            if (methodCall.hasArgument("EMAIL")) {
                email = methodCall.argument("EMAIL").toString();
                //Toast.makeText(activity, email, Toast.LENGTH_SHORT).show();
            }

            String amount = null;
            if (methodCall.hasArgument("AMOUNT")) {
                amount = methodCall.argument("AMOUNT").toString();
                //Toast.makeText(activity, amount, Toast.LENGTH_SHORT).show();
            }

            String currency = null;
            if (methodCall.hasArgument("CURRENCY")) {
                currency = methodCall.argument("CURRENCY").toString();
                //Toast.makeText(activity, currency, Toast.LENGTH_SHORT).show();
            }

            String paymentfor = null;
            if (methodCall.hasArgument("PAYMENT_FOR")) {
                paymentfor = methodCall.argument("PAYMENT_FOR").toString();
                //Toast.makeText(activity, paymentfor, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(context, CardEditActivity.class);
            intent.putExtra(CreditCardUtils.PAYSTACK_API, paystack_public_key);
            intent.putExtra(CreditCardUtils.BACKEND_URL, backend_url);
            intent.putExtra(EMAIL, email);
            intent.putExtra(CreditCardUtils.AMOUNT, amount);
            intent.putExtra(CreditCardUtils.CURRENCY, currency);
            intent.putExtra(CreditCardUtils.PAYMENT_FOR, paymentfor);
            intent.putExtra(CreditCardUtils.POSITION, 0);
            activity.startActivityForResult(intent, PAYSTACK_REQUEST_CODE);
        } else {
            result.notImplemented();
        }
    }


    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYSTACK_REQUEST_CODE) {
            if (data != null && resultCode == RESULT_OK) {
                pendingResult.success("SUCCESSFULL");
            } else {
                pendingResult.error("UNSUCCESSFULL","UNSUCCESSFULL","UNSUCCESSFULL");
                //Toast.makeText(activity, "Bck pressed", Toast.LENGTH_SHORT).show();
            }
            pendingResult = null;
            methodCall = null;
            return true;
        }
        return false;
    }


}
