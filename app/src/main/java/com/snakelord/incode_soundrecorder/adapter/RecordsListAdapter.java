package com.snakelord.incode_soundrecorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.snakelord.incode_soundrecorder.R;
import com.snakelord.incode_soundrecorder.RecordsListFragment;
import com.snakelord.incode_soundrecorder.interfaces.OnRecordListener;
import com.snakelord.incode_soundrecorder.workingFolder.WorkWithFiles;
import java.io.File;

public final class RecordsListAdapter extends RecyclerView.Adapter<RecordsListAdapter.RecordsViewHolder> {

    private File[] mRecordList;
    private RecordsListFragment mOnRecordListener;

    public RecordsListAdapter(RecordsListFragment onRecordListener) {
        this.mOnRecordListener = onRecordListener;
    }

    @NonNull
    @Override
    public RecordsListAdapter.RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false), mOnRecordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        holder.getRecord(mRecordList[position]);
        holder.bind();
    }

    @Override
    public int getItemCount() { return mRecordList.length; }

    public void setFileList(File[] newRecordsList) { mRecordList = newRecordsList; }

   static final class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private File mRecord;
        private TextView mRecordNameTextView;
        private TextView mRecordDurationTextView;
        private OnRecordListener mOnRecordListener;

       RecordsViewHolder(@NonNull View itemView, RecordsListFragment onRecordListener) {
            super(itemView);
            mRecordNameTextView = itemView.findViewById(R.id.record_name);
            mRecordDurationTextView = itemView.findViewById(R.id.record_duration);
            ConstraintLayout recordItem = itemView.findViewById(R.id.record_item);
            this.mOnRecordListener = onRecordListener;
            recordItem.setOnLongClickListener(this);
            recordItem.setOnClickListener(this);
        }

        void bind() {
            mRecordNameTextView.setText(mRecord.getName());
            mRecordDurationTextView.setText(WorkWithFiles.getAudioDuration(mRecord));
        }

       void getRecord(File record) { this.mRecord = record; }

       @Override
       public boolean onLongClick(View v) {
            mOnRecordListener.onRecordLongClick(getAdapterPosition());
            return true;
       }

       @Override
       public void onClick(View v) { mOnRecordListener.onRecordClick(getAdapterPosition()); }
   }

}
