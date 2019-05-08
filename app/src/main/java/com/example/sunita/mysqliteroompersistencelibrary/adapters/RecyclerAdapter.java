package com.example.sunita.mysqliteroompersistencelibrary.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sunita.mysqliteroompersistencelibrary.R;
import com.example.sunita.mysqliteroompersistencelibrary.models.Note;
import com.example.sunita.mysqliteroompersistencelibrary.utils.IMyLongPress;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Note> noteArrayList;
    private IMyLongPress iMyLongClick;

    public void setLongClick(IMyLongPress longClickListener) {
        iMyLongClick = longClickListener;
    }

    public void addData(ArrayList<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
        notifyDataSetChanged();
    }

    public void addSingleData(Note note) {
        noteArrayList.add(note);
        notifyDataSetChanged();
    }

    public void removeNote(int position) {
        noteArrayList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = noteArrayList.get(position);
        if (note != null) {
            holder.getTvDot().setText(Html.fromHtml("&#8226;"));
            if (note.getNotes() != null) {
                holder.getTv_note().setText(note.getNotes());
            }
            holder.getTvTime().setText(note.getTime());
            holder.getViewForeground().setOnLongClickListener(v -> {
                if (iMyLongClick != null) {
                    iMyLongClick.onLongPress(note);
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dot)
        TextView tv_dot;
        @BindView(R.id.tv_note)
        TextView tv_note;

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.view_foreground)
        RelativeLayout viewForeground;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        TextView getTvDot() {
            return tv_dot;
        }

        TextView getTv_note() {
            return tv_note;
        }

        TextView getTvTime() {
            return tvTime;
        }

        RelativeLayout getViewForeground() {
            return viewForeground;
        }
    }
}
