package com.example.codelearningpuzzle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {

    private final List<String> lines;
    private final OnLineClickListener clickListener;

    public interface OnLineClickListener {
        void onLineClick(int position);
    }

    public CodeAdapter(List<String> lines, OnLineClickListener clickListener) {
        this.lines = lines;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаем временную заглушку-текст, чтобы не плодить XML-ошибки
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(32, 24, 32, 24);
        textView.setTextSize(16);
        textView.setMaxLines(1);
        return new CodeViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        holder.tvCodeLine.setText(lines.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onLineClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    public static class CodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodeLine;

        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodeLine = (TextView) itemView;
        }
    }
}
