package com.example.realestatemanager.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.realestatemanager.databinding.EstateListItemBinding;
import com.example.realestatemanager.modele.Property;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class EstateListAdapter extends RecyclerView.Adapter<EstateListAdapter.PropertyViewHolder> {

    private List<Property> propertyList;
    private final Consumer<Property> onItemClickListener;

    public EstateListAdapter(List<Property> propertyList, Consumer<Property> onItemClickListener) {
        this.propertyList = propertyList;
        this.onItemClickListener = onItemClickListener;
    }

    @NotNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final EstateListItemBinding itemBinding =
                EstateListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PropertyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        final Property property = propertyList.get(position);
        holder.propertyListItemBinding.setProperty(property);
        Glide.with(holder.propertyListItemBinding.getRoot())
                .load(property.getMainPhotoUrl())
                .centerCrop()
                .into(holder.propertyListItemBinding.photo);
        holder
                .propertyListItemBinding
                .getRoot()
                .setOnClickListener(__ -> onItemClickListener.accept(property));
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public void updateList(List<Property> newProperties) {
        propertyList = newProperties;
        notifyDataSetChanged();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final EstateListItemBinding propertyListItemBinding;

        public PropertyViewHolder(@NotNull EstateListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            propertyListItemBinding = itemBinding;
        }
    }
}
