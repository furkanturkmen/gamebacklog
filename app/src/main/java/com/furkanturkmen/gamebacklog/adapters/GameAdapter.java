package com.furkanturkmen.gamebacklog.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.furkanturkmen.gamebacklog.R;
import com.furkanturkmen.gamebacklog.models.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {


    private List<Game> mGames;
    final private GameClickListener mGameClickListener;

    public interface GameClickListener{
        void GameOnClick (int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView gameTitle;
        private TextView gamePlatform;
        // private TextView gameNotes;
        private TextView gameStatus;
        private TextView gameDate;

        public ViewHolder(View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitleTV);
            gamePlatform = itemView.findViewById(R.id.gamePlatformTV);
            // gameTitle = itemView.findViewById(R.id.gameNotesTV);
            gameStatus = itemView.findViewById(R.id.gameStatusTV);
            gameDate = itemView.findViewById(R.id.gameDateTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mGameClickListener.GameOnClick(clickedPosition);
        }
    }

    public GameAdapter(List<Game> games, GameClickListener mGameClickListener) {
        this.mGames = games;
        this.mGameClickListener = mGameClickListener;
    }

    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_game, null);

        // Return a new holder instance

        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {

        Game game = mGames.get(position);

        holder.gameTitle.setText(game.getTitle());
        holder.gamePlatform.setText(game.getPlatform());
        holder.gameStatus.setText(game.getStatus());
        holder.gameDate.setText(game.getDate());

    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void swapList (List<Game> newList) {
        mGames = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}