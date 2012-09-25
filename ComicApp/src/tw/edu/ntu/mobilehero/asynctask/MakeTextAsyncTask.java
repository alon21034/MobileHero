package tw.edu.ntu.mobilehero.asynctask;

import tw.edu.ntu.mobilehero.view.DrawView;
import tw.edu.ntu.mobilehero.view.Scrap;
import tw.edu.ntu.mobilehero.view.TextScrap;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

public class MakeTextAsyncTask extends AsyncDialogTask<Intent, Scrap, Throwable> {

    private DrawView mStageView;
    private TextScrap mText;

    public MakeTextAsyncTask(DrawView stageView) {
        this(stageView, null);
    }

    public MakeTextAsyncTask(DrawView stageView, TextScrap textScrap) {
        mStageView = stageView;
        mText = textScrap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute(mStageView.getContext(), "loading");
    }

    @Override
    protected Throwable doInBackground(Intent... params) {
        Log.d("!!","make text asynctask");
//        if (params == null) {
//            return null;
//        }

//        Intent param = params[0];

//        String text_string = param.getStringExtra(TextActivity.TEXT_EXTRA_STRING);
//        String text_font = param.getStringExtra(TextActivity.TEXT_EXTRA_FONT);
//        int text_color = param.getIntExtra(TextActivity.TEXT_EXTRA_COLOR, TextActivity.TEXT_DEFAULT_COLOR);
//        int text_outline_color = param.getIntExtra(TextActivity.TEXT_EXTRA_OUTLINE_COLOR, TextActivity.TEXT_DEFAULT_OUTLINE_COLOR);

        String text_string = "test";
        String text_font = "";
        int text_color = Color.BLACK;
        int text_outline_color = Color.BLACK;
        
//        if (text_string == null) {
//            return null;
//        }

        try {
            if (!isCancelled()) {
                if (mText==null) {
                    // add a new Text in mStageView
                    publishProgress(new TextScrap(mStageView.getContext(), text_string, text_font, text_color, text_outline_color));
                } else {
                    // change the Text
                    mText.setText(mStageView, text_string, text_font, text_color, text_outline_color);
                    mText.makeOnScreen();
                    mStageView.setChanged(true);
                }
            }
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Scrap... imgs) {
        // there will be only one Img when adding Text
        if (mText==null)
            mStageView.addScrap(imgs[0]);
    }

    @Override
    protected void onPostExecute(Throwable e) {
        super.onPostExecute(e);
        if (e != null) {
            e.printStackTrace();
            new AlertDialog.Builder(mStageView.getContext())
                    .setTitle(android.R.string.dialog_alert_title)
                    .setMessage(e.getLocalizedMessage()).show();
        }
    }

}