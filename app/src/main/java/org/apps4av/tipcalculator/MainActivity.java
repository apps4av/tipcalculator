package org.apps4av.tipcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextBillAmount;
    private EditText mEditTextSplitBetween;
    private TextView mTextViewExactMinimum;
    private TextView mTextViewExactUsual;
    private TextView mTextViewExactHigh;
    private TextView mTextViewRoundedMinimum;
    private TextView mTextViewRoundedUsual;
    private TextView mTextViewRoundedHigh;
    private TextView mTextViewTipMinimum;
    private TextView mTextViewTipUsual;
    private TextView mTextViewTipHigh;

    private Preferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = new Preferences(this);

        /*
         * Do all logic here in on create by callbacks
         */

        mEditTextBillAmount = (EditText)findViewById(R.id.activity_main_edittext_bill_amount);
        mEditTextSplitBetween = (EditText)findViewById(R.id.activity_main_edittext_split_between);
        mTextViewExactMinimum = (TextView)findViewById(R.id.activity_main_textview_exact_minimum);
        mTextViewExactUsual = (TextView)findViewById(R.id.activity_main_textview_exact_usual);
        mTextViewExactHigh = (TextView)findViewById(R.id.activity_main_textview_exact_high);
        mTextViewRoundedMinimum = (TextView)findViewById(R.id.activity_main_textview_rounded_minimum);
        mTextViewRoundedUsual = (TextView)findViewById(R.id.activity_main_textview_rounded_usual);
        mTextViewRoundedHigh = (TextView)findViewById(R.id.activity_main_textview_rounded_high);
        mTextViewTipMinimum = (TextView)findViewById(R.id.activity_main_text_view_minimum);
        mTextViewTipUsual = (TextView)findViewById(R.id.activity_main_text_view_usual);;
        mTextViewTipHigh = (TextView)findViewById(R.id.activity_main_text_view_high);;

        mEditTextBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                change();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditTextSplitBetween.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                change();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        // Setup in a known state
        change();
    }

    private void clearAllViews() {

        mTextViewExactMinimum.setText("");
        mTextViewExactUsual.setText("");
        mTextViewExactHigh.setText("");
        mTextViewRoundedMinimum.setText("");
        mTextViewRoundedUsual.setText("");
        mTextViewRoundedHigh.setText("");
    }


    /**
     * Round a number to two decimal digits
     * @param number
     * @return
     */
    private String round100(double number) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        return df.format(Math.round(number * 100) / 100.0);
    }

    /**
     * Input changed, change views
     */
    private void change() {

        // clear
        clearAllViews();

        // get input
        double billAmount;
        int splitBetweenInt;
        int minimumTip;
        int usualTip;
        int highTip;

        try {
            billAmount = Double.parseDouble(mEditTextBillAmount.getText().toString());
        }
        catch (Exception e1) {
            // invalid input
            return;
        }

        try {
            splitBetweenInt = Integer.parseInt(mEditTextSplitBetween.getText().toString());
        }
        catch (Exception e1) {
            // invalid input
            Toast.makeText(this, R.string.invalid_parties, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            minimumTip = Integer.parseInt(mPref.getMinimumTip());
        }
        catch (Exception e1) {
            // invalid input
            Toast.makeText(this, R.string.invalid_minimum, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            usualTip = Integer.parseInt(mPref.getUsualTip());
        }
        catch (Exception e1) {
            // invalid input
            Toast.makeText(this, R.string.invalid_usual, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            highTip = Integer.parseInt(mPref.getHighTip());
        }
        catch (Exception e1) {
            // invalid input
            Toast.makeText(this, R.string.invalid_high, Toast.LENGTH_SHORT).show();
            return;
        }

        mTextViewTipMinimum.setText(minimumTip + "%");
        mTextViewTipUsual.setText(usualTip + "%");
        mTextViewTipHigh.setText(highTip + "%");

        // now calculate
        double minTipFraction = (double)minimumTip / 100.0;
        double usualTipFraction = (double)usualTip / 100.0;
        double highTipFraction = (double)highTip / 100.0;
        double split  = (double)splitBetweenInt;

        // find 12 values
        double minTipExact = minTipFraction * billAmount / split;
        double minTotalExact = minTipExact + billAmount / split;

        double usualTipExact = usualTipFraction * billAmount / split;
        double usualTotalExact = usualTipExact + billAmount / split;

        double highTipExact = highTipFraction * billAmount / split;
        double highTotalExact = highTipExact + billAmount / split;


        double minTotalRounded = Math.ceil(minTotalExact);
        double minTipRounded = minTotalRounded - billAmount / split;

        double usualTotalRounded = Math.ceil(usualTotalExact);
        double usualTipRounded = usualTotalRounded - billAmount / split;

        double highTotalRounded = Math.ceil(highTotalExact);
        double highTipRounded = highTotalRounded - billAmount / split;


        // set in view
        mTextViewExactMinimum.setText("Tip " + round100(minTipExact) + "\nTot " + round100(minTotalExact));
        mTextViewExactUsual.setText("Tip " + round100(usualTipExact) + "\nTot " + round100(usualTotalExact));
        mTextViewExactHigh.setText("Tip " + round100(highTipExact) + "\nTot " + round100(highTotalExact));

        mTextViewRoundedMinimum.setText("Tip " + round100(minTipRounded) + "\nTot " + round100(minTotalRounded));
        mTextViewRoundedUsual.setText("Tip " + round100(usualTipRounded) + "\nTot " + round100(usualTotalRounded));
        mTextViewRoundedHigh.setText("Tip " + round100(highTipRounded) + "\nTot " + round100(highTotalRounded));


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            // Show settings dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.dialog_settings, null);
            builder.setView(dialoglayout);

            final EditText minimum = (EditText) dialoglayout.findViewById(R.id.dialog_settings_edittext_minimum);
            minimum.setText(mPref.getMinimumTip());
            final EditText usual = (EditText) dialoglayout.findViewById(R.id.dialog_settings_edittext_usual);
            usual.setText(mPref.getUsualTip());
            final EditText high = (EditText) dialoglayout.findViewById(R.id.dialog_settings_edittext_high);
            high.setText(mPref.getHighTip());

            builder.setTitle(getString(R.string.settings))
                    .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            mPref.setMinimumTip(minimum.getText().toString());
                            mPref.setUsualTip(usual.getText().toString());
                            mPref.setHighTip(high.getText().toString());

                            change();
                        }
                    });

            // set focus to OK
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setFocusable(true);
                    positive.setFocusableInTouchMode(true);
                    positive.requestFocus();
                }
            });
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
