package com.snakelord.soundrecorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.snakelord.soundrecorder.R;
import com.snakelord.soundrecorder.RecordsListFragment;
import com.snakelord.soundrecorder.interfaces.OnRecordListener;
import com.snakelord.soundrecorder.workingFolder.WorkWithFiles;
import java.io.File;

public final class RecordsListAdapter extends RecyclerView.Adapter<RecordsListAdapter.RecordsViewHolder> {

    private File[] recordList;
    private RecordsListFragment onRecordListener;

    public RecordsListAdapter(RecordsListFragment onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    @NonNull
    @Override
    public RecordsListAdapter.RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false), onRecordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsViewHolder holder, int position) {
        holder.getRecord(recordList[position]);
        holder.bind();
    }

    @Override
    public int getItemCount() { return recordList.length; }

    public void setFileList(File[] newRecordsList) { recordList = newRecordsList; }

   static final class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private File record;
        private TextView recordNameTextView;
        private TextView recordDurationTextView;
        private OnRecordListener onRecordListener;

       RecordsViewHolder(@NonNull View itemView, RecordsListFragment onRecordListener) {
            super(itemView);
            recordNameTextView = itemView.findViewById(R.id.record_name);
            recordDurationTextView = itemView.findViewById(R.id.record_duration);
            ConstraintLayout recordItem = itemView.findViewById(R.id.record_item);
            this.onRecordListener = onRecordListener;
            recordItem.setOnLongClickListener(this);
            recordItem.setOnClickListener(this);
        }

        void bind() {
            recordNameTextView.setText(record.getName());
            recordDurationTextView.setText(WorkWithFiles.getAudioDuration(record));
        }

       void getRecord(File record) { this.record = record; }

       @Override
       public boolean onLongClick(View v) {
            onRecordListener.onRecordLongClick(getAdapterPosition());
            return true;
       }

       @Override
       public void onClick(View v) { onRecordListener.onRecordClick(getAdapterPosition()); }
   }

}
