package com.choco_tyranno.team_tree.presentation.detail_page;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.choco_tyranno.team_tree.R;

public class OnClickListenerForLoadContactInfoFab implements View.OnClickListener {
    public static final int REQUEST_LOAD_CONTACT_INFO = 30;

    @Override
    public void onClick(View v) {
        DetailCardActivity detailCardActivity = ((DetailCardActivity) v.getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(detailCardActivity);
        builder.setTitle("-연락처 불러오기-")
                .setMessage("저장된 연락처를 불러오시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                            detailCardActivity.startActivityForResult(intent, REQUEST_LOAD_CONTACT_INFO);
                            dialog.cancel();
                        }
                )
                .setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(v.getResources().getColor(R.color.defaultTextColor, v.getContext().getTheme()));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(v.getResources().getColor(R.color.defaultTextColor, v.getContext().getTheme()));
        detailCardActivity.getDetailFab().animateFab(detailCardActivity.getBinding());
    }
}
