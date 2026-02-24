package ucas.recipebook.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ucas.recipebook.R;
import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.databinding.ItemRecipeBinding;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes != null ? recipes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeBinding binding = ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecipeBinding binding;

        RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRecipeClick(recipes.get(position));
                }
            });
        }

        void bind(Recipe recipe) {
            binding.tvTitle.setText(recipe.getTitle());
            binding.tvCategory.setText(recipe.getCategory());

            if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(recipe.getImageUrl())
                        .placeholder(R.drawable.ic_recipe_placeholder)
                        .centerCrop()
                        .into(binding.ivRecipeImage);
            } else {
                binding.ivRecipeImage.setImageResource(R.drawable.ic_recipe_placeholder);
                binding.ivRecipeImage.setScaleType(android.widget.ImageView.ScaleType.CENTER);
            }
        }
    }
}
