package com.example.finalexam2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Task> _tasks;
    private OnNoteListener mOnNoteListener;
    private Context _context;
    private Cursor _cu;
    private LinearLayout l;

    public RecyclerAdapter(ArrayList<Task> tasks, OnNoteListener mOnNoteListener, Context context, Cursor _cu) {
        this._tasks = tasks;
        this.mOnNoteListener = mOnNoteListener;
        this._context = context;
        this._cu = _cu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(_context);
        View v = li.inflate(R.layout.row, parent, false);

        return new ViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(_tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return _cu.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnNoteListener onNoteListener;
        private ImageView _imagen;
        private TextView _tv1, _tv2;
        private CheckBox _ch;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this._imagen = itemView.findViewById(R.id.imageView);
            this._tv1 = itemView.findViewById(R.id.taskid);
            this._tv2 = itemView.findViewById(R.id.taskdate);
            this._ch = itemView.findViewById(R.id.checkBox);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Task t) {
            Bitmap bmp = BitmapFactory.decodeByteArray(t.getImage(), 0, t.getImage().length);

            this._imagen.setImageBitmap(bmp);
            this._tv1.setText(t.getName());
            this._tv2.setText(t.getDate());
            if (t.getDone().equals("true")) {
                this._ch.setChecked(true);
                this._ch.setEnabled(false);
            } else this._ch.setChecked(false);
            _ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (_context instanceof MainActivity) {
                        ((MainActivity) _context).updateAdapter(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public ArrayList<Task> getTasks() {
        return _tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this._tasks = tasks;
    }

    public Cursor get_cu() {
        return _cu;
    }

    public void set_cu(Cursor _cu) {
        this._cu = _cu;
    }

}
