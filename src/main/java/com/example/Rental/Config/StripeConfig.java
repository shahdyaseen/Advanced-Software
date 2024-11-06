package com.example.Rental.Config;

import com.stripe.Stripe;

public class StripeConfig {
    public static void init() {
        Stripe.apiKey = "sk_test_51QHXq0E6bYKa0K42YrlfLWGL2OQBNpp875TeTzgfPqMMGSljXtAdlUQUnXv0r9ZLpUwIbiWnoEcPF4HNIivQcmVu00cb4NO281";  // استبدل بمفتاح Stripe الخاص بك
    }
}
