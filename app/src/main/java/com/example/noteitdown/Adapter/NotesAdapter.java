package com.example.noteitdown.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteitdown.Activity.AddNotesActivity;
import com.example.noteitdown.Activity.MainActivity;
import com.example.noteitdown.Activity.UpdateNotesActivity;
import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    Context context;
    List<Notes> list;

    public NotesAdapter(Context context, List<Notes> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Subtitle,Date,Note;
        ImageView Indicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.Title_item);
            Subtitle = itemView.findViewById(R.id.SubTitle_item);
            Date = itemView.findViewById(R.id.date_item);
            Note = itemView.findViewById(R.id.note_item);
            Indicator = itemView.findViewById(R.id.priority_indicator_item);

        }
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Notes note = list.get(position);
        holder.Title.setText(note.getNote_Title());
        holder.Subtitle.setText(note.getNoteSubtitle());
        holder.Note.setText(note.getNote());
        holder.Date.setText(note.getNoteDate());

        switch(note.getNotePriority()){
            case "1":
                holder.Indicator.setImageResource(R.drawable.green_circle);
                break;
            case "2":
                holder.Indicator.setImageResource(R.drawable.yellow_circle);
                break;
            case "3":
                holder.Indicator.setImageResource(R.drawable.red_circle);
                break;
            default:
                holder.Indicator.setImageResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateNotesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID",note.getId());
                intent.putExtra("Title",note.getNote_Title());
                intent.putExtra("Subtitle",note.getNoteSubtitle());
                intent.putExtra("Note",note.getNote());
                intent.putExtra("Priority",note.getNotePriority());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
