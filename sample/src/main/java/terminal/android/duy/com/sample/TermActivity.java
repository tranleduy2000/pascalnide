package terminal.android.duy.com.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.TextKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import terminal.android.duy.com.terminal_view.EmulatorView;
import terminal.android.duy.com.terminal_view.TermSession;


/**
 * This sample activity demonstrates the use of EmulatorView.
 * <p>
 * This activity also demonstrates how to set up a simple TermSession connected
 * to a local program.  The Telnet connection demonstrates a more complex case;
 * see the TelnetSession class for more details.
 */
public class TermActivity extends MainActivity {
    final private static String TAG = "TermActivity";
    private static final int MSG_CONNECTED = 1;
    private EditText mEntry;
    private EmulatorView mEmulatorView;
    private TermSession mSession;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Text entry box at the bottom of the activity.  Note that you can
           also send input (whether from a hardware device or soft keyboard)
           directly to the EmulatorView. */
        mEntry = (EditText) findViewById(R.id.term_entry);
        mEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int action, KeyEvent ev) {
                // Ignore enter-key-up events
                if (ev != null && ev.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                }
                // Don't try to send something if we're not connected yet
                TermSession session = mSession;
                if (mSession == null) {
                    return true;
                }

                Editable e = (Editable) v.getText();
                // Write to the terminal session
                session.write(e.toString());
                session.write('\r');
                TextKeyListener.clear(e);
                return true;
            }
        });

        /* Sends the content of the text entry box to the terminal, without
           sending a carriage return afterwards */
        Button sendButton = (Button) findViewById(R.id.term_entry_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Don't try to send something if we're not connected yet
                TermSession session = mSession;
                if (mSession == null) {
                    Log.d(TAG, "onClick: session null");
                    return;
                }
                Editable e = (Editable) mEntry.getText();
                session.write(e.toString());
                TextKeyListener.clear(e);
            }
        });

        /**
         * EmulatorView setup.
         */
        EmulatorView view = (EmulatorView) findViewById(R.id.emulatorView);
        mEmulatorView = view;

        /* Let the EmulatorView know the screen's density. */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view.setDensity(metrics);

        /* Create a TermSession. */
        Intent myIntent = getIntent();
        TermSession session;
        // Create a local shell session.
        session = createLocalTermSession();
        if (session == null) {
            finish();
            return;
        }
        mSession = session;

        /* Attach the TermSession to the EmulatorView. */
        view.attachSession(session);

        /* That's all you have to do!  The EmulatorView will call the attached
           TermSession's initializeEmulator() automatically, once it can
           calculate the appropriate screen size for the terminal emulator. */
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* You should call this to let EmulatorView know that it's visible
           on screen. */
        mEmulatorView.onResume();
        mEntry.requestFocus();
    }

    @Override
    protected void onPause() {
        /* You should call this to let EmulatorView know that it's no longer
           visible on screen. */
        mEmulatorView.onPause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /**
         * Finish the TermSession when we're destroyed.  This will free
         * resources, stop I/O threads, and close the I/O streams attached
         * to the session.
         *
         * For the local session, closing the streams will kill the shell; for
         * the Telnet session, it closes the network connection.
         */
        if (mSession != null) {
            mSession.finish();
        }

        super.onDestroy();
    }

    /**
     * Create a TermSession connected to a local shell.
     */
    private TermSession createLocalTermSession() {
        /* Instantiate the TermSession ... */
        TermSession session = new TermSession();

        /* ... create a process ... */
        /* TODO:Make local session work without execpty.
        String execPath = LaunchActivity.getDataDir(this) + "/bin/execpty";
        ProcessBuilder execBuild =
                new ProcessBuilder(execPath, "/system/bin/sh", "-");
        */
        ProcessBuilder execBuild =
                new ProcessBuilder("/system/bin/sh", "-");
        execBuild.redirectErrorStream(true);
        Process exec = null;
        try {
            exec = execBuild.start();
        } catch (Exception e) {
            Log.e(TAG, "Could not start terminal process.", e);
            return null;
        }

        /* ... and connect the process's I/O streams to the TermSession. */
        session.setTermIn(exec.getInputStream());
        session.setTermOut(exec.getOutputStream());

        /* You're done! */
        return session;

        /**
         * NB: You can invoke a program without using execpty or a native code
         * method, but the results may not be what you expect, because the
         * process will be connected to a pipe, not a tty device.  tty devices
         * provide services such as flow control and input/output translation
         * which many programs expect.
         *
         * If you do connect a program directly to a TermSession without using
         * a tty, you should probably at a minimum translate '\r' (sent by the
         * Enter key) to '\n' (which most programs expect as their newline
         * input) in write(), and translate '\n' (sent by most programs to
         * indicate a newline) to '\r\n' (which the terminal emulator needs to
         * actually start a new line without overdrawing text or "staircase
         * effect") in processInput(), before sending it to the terminal
         * emulator.
         *
         * For an example of how to obtain and use a tty device in native code,
         * see assets-src/execpty.c.
         */
    }


}
