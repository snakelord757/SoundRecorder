package com.snakelord.incode_soundrecorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.snakelord.incode_soundrecorder.adapter.RecordsListAdapter;
import com.snakelord.incode_soundrecorder.dialogs.BottomSheetPlayer;
import com.snakelord.incode_soundrecorder.dialogs.OptionsDialogFragment;
import com.snakelord.incode_soundrecorder.workingFolder.WorkingFolder;
import com.snakelord.incode_soundrecorder.interfaces.DialogCallback;
import com.snakelord.incode_soundrecorder.interfaces.OnRecordListener;

public final class RecordsListFragment extends Fragment implements OnRecordListener, DialogCallback {

    private View mRecordsListView;
    private RecyclerView mRecordsRecyclerView;
    private RecordsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecordsListView = inflater.inflate(R.layout.fragment_rercords_list, container, false);
        mRecordsRecyclerView = mRecordsListView.findViewById(R.id.records_recycler_view);
        checkIsFolderEmpty();
        initRecordsRecyclerView();
        return mRecordsListView;
    }

    private void showEmptyFolderPlaceholder(View parent) {
        ViewStub emptyFolderPlaceholder = parent.findViewById(R.id.empty_folder_view_stub);
        emptyFolderPlaceholder.inflate();
    }

    private void initRecordsRecyclerView() {
        adapter = new RecordsListAdapter( this);
        adapter.setFileList(new WorkingFolder(getContext()).getRecordsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecordsRecyclerView.setAdapter(adapter);
        mRecordsRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onRecordLongClick(int position) {
        OptionsDialogFragment dialog = OptionsDialogFragment.newInstance(new WorkingFolder(getContext()).getRecordsList()[position].getAbsolutePath());
        dialog.setDialogCallback(this);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onRecordClick(int position) {
        BottomSheetPlayer bottomSheetPlayer = BottomSheetPlayer.newInstance(new WorkingFolder(getContext()).getRecordsList()[position].getAbsolutePath());
        bottomSheetPlayer.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void updateRecyclerView() {
        adapter.setFileList(new WorkingFolder(getContext()).getRecordsList());
        adapter.notifyDataSetChanged();
        checkIsFolderEmpty();
    }

    private void checkIsFolderEmpty() { if (new WorkingFolder(getContext()).isFolderEmpty())  showEmptyFolderPlaceholder(mRecordsListView); }
}
