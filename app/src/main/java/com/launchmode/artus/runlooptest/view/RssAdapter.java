/*
 * Copyright (c) 2018 Phunware Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.launchmode.artus.runlooptest.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.launchmode.artus.runlooptest.R;
import com.launchmode.artus.runlooptest.databinding.ListItemBinding;
import com.launchmode.artus.runlooptest.model.RssEntry;
import com.launchmode.artus.runlooptest.viewmodel.RssItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycle view adapter
 */
public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder> {
    private List<RssEntry> data;
    public MutableLiveData<RssEntry> clicked = new MutableLiveData<>();

    public RssAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public RssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                new FrameLayout(parent.getContext()), false);
        return new RssViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RssViewHolder holder, int position) {
        final RssEntry item = data.get(position);
        RssItemViewModel viewModel = new RssItemViewModel(item);
        viewModel.getClicked().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                clicked.setValue(item);
            }
        });
        holder.setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public void onViewAttachedToWindow(RssViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(RssViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void updateData(@Nullable List<RssEntry> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    static class RssViewHolder extends RecyclerView.ViewHolder {
        ListItemBinding binding;

        RssViewHolder(View itemView) {
            super(itemView);
            bind();
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind(); // Don't forget to unbind
            }
        }

        void setViewModel(RssItemViewModel viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }
    }
}
