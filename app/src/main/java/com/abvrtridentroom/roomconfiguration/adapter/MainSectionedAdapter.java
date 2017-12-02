package com.abvrtridentroom.roomconfiguration.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abvrtridentroom.roomconfiguration.R;
import com.abvrtridentroom.roomconfiguration.ui.SlideShowActivity;

public class MainSectionedAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private String[] leftStr;
    private String[][] rightStr;

    public MainSectionedAdapter(Context context, String[] leftStr, String[][] rightStr) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rightStr = rightStr;
    }

    @Override
    public Object getItem(int section, int position) {
        return rightStr[section][position];
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length;
    }

    @Override
    public int getCountForSection(int section) {
        return rightStr[section].length;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.row_grid_gallery_cell, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textView_name0)).setText(rightStr[section][position]+"01");
        ((TextView) layout.findViewById(R.id.textView_name1)).setText(rightStr[section][position]+"02");
        ((TextView) layout.findViewById(R.id.textView_name2)).setText(rightStr[section][position]+"03");
        ((TextView) layout.findViewById(R.id.textView_name3)).setText(rightStr[section][position]+"04");

//        int drawableResourceId = mContext.getResources().getIdentifier(textureData.getImage(), "mipmap", mContext.getPackageName());
//        ((ImageView) layout.findViewById(R.id.imageView_gallery0)).setImageResource(drawableResourceId);

        ((ImageView) layout.findViewById(R.id.imageView_gallery0)).setImageDrawable(mContext.getResources().getDrawable(R.mipmap.screen01));
        ((ImageView) layout.findViewById(R.id.imageView_gallery1)).setImageDrawable(mContext.getResources().getDrawable(R.mipmap.screen02));
        ((ImageView) layout.findViewById(R.id.imageView_gallery2)).setImageDrawable(mContext.getResources().getDrawable(R.mipmap.screen03));
        ((ImageView) layout.findViewById(R.id.imageView_gallery3)).setImageDrawable(mContext.getResources().getDrawable(R.mipmap.screen01));

        layout.findViewById(R.id.relativeLayout_card0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlideShowActivity.class);
                mContext.startActivity(intent);
            }
        });

        layout.findViewById(R.id.relativeLayout_card1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlideShowActivity.class);
                mContext.startActivity(intent);
            }
        });

        layout.findViewById(R.id.relativeLayout_card2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlideShowActivity.class);
                mContext.startActivity(intent);
            }
        });

        layout.findViewById(R.id.relativeLayout_card3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlideShowActivity.class);
                mContext.startActivity(intent);
            }
        });
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.row_header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        return layout;
    }

}
