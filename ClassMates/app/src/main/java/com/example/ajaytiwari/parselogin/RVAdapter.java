package com.example.ajaytiwari.parselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    String branch;
    String organi;
    String desigina;
    String location;
    String company;
    List<Profiles> mItems;
    List<ParseUser> _parseUser = new ArrayList<>();

    public RVAdapter(List<ParseUser> parseUsers) {
        super();
        _parseUser = parseUsers;
        mItems = new ArrayList<Profiles>();
        for (int index = 0; index < _parseUser.size(); index++) {
            final Profiles profiles = new Profiles();
            profiles.setName(_parseUser.get(index).get("name").toString());
            profiles.setStatus(_parseUser.get(index).getEmail());
            profiles.setDevice_id(_parseUser.get(index));
            profiles.setDevice_id(_parseUser.get(index));
            profiles.setCollege(_parseUser.get(index).get("college").toString());
            profiles.setLocation(_parseUser.get(index).get("location").toString());
            profiles.setProfession(_parseUser.get(index).get("profession").toString());
            profiles.setOrgani(_parseUser.get(index).get("organization").toString());
            profiles.setBatch(_parseUser.get(index).getString("batch"));
            profiles.setMobile(_parseUser.get(index).getString("mobile"));
            profiles.setEducation(_parseUser.get(index).getString("education"));
            profiles.setEdu_help(_parseUser.get(index).getBoolean("educationHelp"));
            profiles.setSales_help(_parseUser.get(index).getBoolean("salesHelp"));
            profiles.setMarketing_help(_parseUser.get(index).getBoolean("marketingHelp"));
            profiles.setGender(_parseUser.get(index).getString("gender"));
            profiles.setFbID(_parseUser.get(index).getString("fbid"));

            ParseFile fileObject = (ParseFile) parseUsers.get(index).get("image");
            if (fileObject != null) {
                fileObject.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, com.parse.ParseException e) {
                        if (e == null) {
                            if (data != null) {

                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                profiles.setBitmap(bmp);
                            }

                        }
                    }

                });
            }

            mItems.add(profiles);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contacts_adapter_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Profiles profiles = mItems.get(i);
        viewHolder.tvNature.setText(profiles.getName());
        viewHolder.batch.setText(profiles.getBatch());
        viewHolder.college.setText(profiles.getCollege());
        viewHolder.profession.setText(profiles.getProfession());
        viewHolder.education = profiles.getEducation();
        viewHolder.mob = profiles.getMobile();
        viewHolder.gender = profiles.getGender();
        viewHolder.locat = profiles.getLocation();
        viewHolder.orgg = profiles.getOrgani();
        if (profiles.getGender().equalsIgnoreCase("female")) {
            viewHolder.gen.setImageResource(R.drawable.female);
        } else {
            viewHolder.gen.setImageResource(R.drawable.male);
        }
        viewHolder.id = profiles.getDevice_id();
        if (profiles.getBitmap() != null) {
            viewHolder.bmp.setImageBitmap(profiles.getBitmap());
        } else {

            if (profiles.getFbID() != null) {

                Picasso
                        .with(viewHolder.itemView.getContext())
                        .load("https://graph.facebook.com/" + profiles.getFbID() + "/picture?type=large")
                        .into(viewHolder.bmp);
            } else
                viewHolder.bmp.setImageResource(R.drawable.a);
        }
        Log.d("Stark", branch + organi + desigina + location + company);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNature;
        public TextView batch;
        public TextView college;
        public TextView profession;
        public String locat;
        public String orgg;
        public String education;
        public ImageView bmp;
        public ImageView gen;
        public ParseUser id;
        public String mob;
        public String gender;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvNature = (TextView) itemView.findViewById(R.id.tv_nature);
            batch = (TextView) itemView.findViewById(R.id.tv_des_nature);
            college = (TextView) itemView.findViewById(R.id.college);
            profession = (TextView) itemView.findViewById(R.id.profession_adp);
            gen = (ImageView) itemView.findViewById(R.id.gen);
            bmp = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), UserDetails.class);
                    intent.putExtra("location", locat);
                    intent.putExtra("name", tvNature.getText().toString());
                    intent.putExtra("education", education);
                    intent.putExtra("organization", orgg);
                    intent.putExtra("profession", profession.getText().toString());
                    intent.putExtra("college", college.getText().toString());
                    intent.putExtra("batch", batch.getText().toString());
                    intent.putExtra("id", id.getObjectId());
                    intent.putExtra("mobile", mob);
                    itemView.getContext().startActivity(intent);


                }
            });
        }
    }

}