package tw.edu.ntu.mobilehero.asynctask;

import tw.edu.ntu.mobilehero.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class AsyncDialogTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected ProgressDialog mDialog;

    protected void onPreExecute(Context context, String message) {
        mDialog = ProgressDialog.show(context, null, message);
    }

    protected void onPreExecute(Context context, int message) {
        mDialog = ProgressDialog.show(context, null, context.getString(message));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onCancelled() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.cancel();
            } catch (IllegalArgumentException e) {
                // java.lang.IllegalArgumentException: View not attached to window manager
            }

            mDialog = null;
        }
    }

    @Override
    protected void onCancelled(Result result) {
        onCancelled();
    }

    @Override
    protected void onPostExecute(Result result) {
        Utils.dismissDialog(mDialog);
        mDialog = null;
    }
}
