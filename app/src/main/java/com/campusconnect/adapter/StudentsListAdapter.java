package com.campusconnect.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.campusconnect.POJO.ModelMakeAdmin;
import com.campusconnect.POJO.ModelStudent;
import com.campusconnect.POJO.MyApi;
import com.campusconnect.R;
import com.campusconnect.auxiliary.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RK on 13/07/2016.
 */
public class StudentsListAdapter extends
        RecyclerView.Adapter<StudentsListAdapter.StudentsListViewHolder> {

    Context context;
    private List<ModelStudent> studentList;
    boolean isAdmin;
    String courseId;
    String profileId;

    public StudentsListAdapter(Context context,List<ModelStudent> students,boolean isAdmin,String courseId,String profileId) {
        this.context = context;
        this.studentList = students;
        this.isAdmin = isAdmin;
        this.courseId = courseId;
        this.profileId = profileId;
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }


    @Override
    public void onBindViewHolder(final StudentsListViewHolder studentsListViewHolder, final int i)
    {
        final ModelStudent student = studentList.get(i);
        studentsListViewHolder.student_name.setText(student.getProfileName());

        if(!isAdmin)
        {
            studentsListViewHolder.make_admin_button.setVisibility(View.GONE);
        }

        if(student.getIsAdmin().equals("1")){
            // student isAdmin
            studentsListViewHolder.make_admin_button.setVisibility(View.VISIBLE);
            if(!isAdmin) {
                studentsListViewHolder.make_admin_button.setEnabled(false);
            }
            else
            {
                if(student.getProfileId().equals(profileId)) studentsListViewHolder.make_admin_button.setEnabled(false);
            }
            studentsListViewHolder.make_admin_button.setChecked(true);
        }
        else
        {
            studentsListViewHolder.make_admin_button.setChecked(false);
        }

        Picasso.with(context).load(student.getPhotoUrl()).placeholder(R.mipmap.ccnoti).error(R.mipmap.ccnoti).into(studentsListViewHolder.student_profile_pic);

        if(i==getItemCount()-1)
        {
            studentsListViewHolder.divider.setVisibility(View.GONE);
            studentsListViewHolder.student_card_all.setPadding(16,16,16,16);
        }
        studentsListViewHolder.make_admin_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MyApi.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MyApi myApi = retrofit.create(MyApi.class);
                MyApi.addAdminRequest body = new MyApi.addAdminRequest(student.getProfileId(),courseId);
                Call<ModelMakeAdmin> call = myApi.addAdmin(body);
                call.enqueue(new Callback<ModelMakeAdmin>() {
                    @Override
                    public void onResponse(Call<ModelMakeAdmin> call, Response<ModelMakeAdmin> response) {


                    }

                    @Override
                    public void onFailure(Call<ModelMakeAdmin> call, Throwable t)
                    {
                    }
                });
                if(student.getIsAdmin().equals("1")){student.setIsAdmin("0");}
                else if(student.getIsAdmin().equals("0")){student.setIsAdmin("1");}


            }
        });

    }


    @Override
    public StudentsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_student, viewGroup, false);
        return new StudentsListViewHolder(itemView);
    }

    public class StudentsListViewHolder extends RecyclerView.ViewHolder {



        @Bind(R.id.student_card)
        CardView student_card;

        @Bind(R.id.iv_student_profile_picture)
        CircularImageView student_profile_pic;

        @Bind(R.id.tv_student_name)
        TextView student_name;

        @Bind(R.id.tb_make_admin)
        ToggleButton make_admin_button;

        @Bind(R.id.divider)
        View divider;

        @Bind(R.id.container_student_card_all)
        RelativeLayout student_card_all;

        public StudentsListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}