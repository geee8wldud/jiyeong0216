package com.example.admin.clothes.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;

import com.example.admin.clothes.R;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.house_icon,
            R.drawable.feedback_icon,
            R.drawable.cheart_icon,
            R.drawable.closet_icon,
            R.drawable.setting_icon
    };
    private int[] selectedtabIcons={
            R.drawable.chouse_icon,
            R.drawable.cfeedback_icon,
            R.drawable.cheart,
            R.drawable.ccloset_icon,
            R.drawable.csetting_icon
    };

    private Fragment fragments[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new Fragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new BoardFragment();
        fragments[2] = new RankFragment();
        fragments[3] = new ClosetFragment();
        fragments[4] = new SettingFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        register();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(fragments[0], "홈");
        adapter.addFrag(fragments[1], "게시판");
        adapter.addFrag(fragments[2], "관심");
        adapter.addFrag(fragments[3], "옷장");
        adapter.addFrag(fragments[4], "설정");
        viewPager.setAdapter(adapter);
    }

    //탭 선택시 이미지 변경
    @Override
    public void onTabChanged(String s) {
        if(tabLayout.getTabAt(0).isSelected()){

            tabLayout.getTabAt(0).setIcon(selectedtabIcons[0]);

        }else if(tabLayout.getTabAt(1).isSelected()){

            tabLayout.getTabAt(1).setIcon(selectedtabIcons[1]);

        }else if(tabLayout.getTabAt(2).isSelected()){

            tabLayout.getTabAt(2).setIcon(selectedtabIcons[2]);

        }else if(tabLayout.getTabAt(3).isSelected()) {

            tabLayout.getTabAt(3).setIcon(selectedtabIcons[3]);

        }else if(tabLayout.getTabAt(4).isSelected()){

            tabLayout.getTabAt(4).setIcon(selectedtabIcons[4]);

        }
    }

    public void register(){
        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tabLayout.getTabAt(0).isSelected()){

                    tabLayout.getTabAt(0).setIcon(selectedtabIcons[0]);
                    //  onTabChanged("홈");

                }else if(tabLayout.getTabAt(1).isSelected()){

                    tabLayout.getTabAt(1).setIcon(selectedtabIcons[1]);

                }else if(tabLayout.getTabAt(2).isSelected()){

                    tabLayout.getTabAt(2).setIcon(selectedtabIcons[2]);

                }else if(tabLayout.getTabAt(3).isSelected()) {

                    tabLayout.getTabAt(3).setIcon(selectedtabIcons[3]);

                }else if(tabLayout.getTabAt(4).isSelected()){

                    tabLayout.getTabAt(4).setIcon(selectedtabIcons[4]);

                }
            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed(); //지워야 실행됨

        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("정말 종료하시겠습니까?");
        d.setPositiveButton("예", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // process전체 종료
                finish();
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        d.show();
    }

/*
    //탭 이미지 변경
    class TabTestListener implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class mClass;

        public TabTestListener(Activity activity, String tag, Class clz)	 {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }


        @Override
        public void onTabSelected(ActionBar.TabActivity tab, FragmentTransaction ft) {
            if(tab.getPosition()==0)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(selectedtabIcons[tab.getPosition()]);
            }
            else if(tab.getPosition()==1)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(selectedtabIcons[tab.getPosition()]);
            }
            else if(tab.getPosition()==2)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(selectedtabIcons[tab.getPosition()]);

            }else if(tab.getPosition()==3){

                tabLayout.getTabAt(tab.getPosition()).setIcon(selectedtabIcons[tab.getPosition()]);
            }


            if (mFragment == null)
            {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.TabActivity tab, FragmentTransaction ft) {
            if(tab.getPosition()==0)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);
            }
            else if(tab.getPosition()==1)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);
            }
            else if(tab.getPosition()==2)
            {
                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);

            }else if(tab.getPosition()==3){

                tabLayout.getTabAt(tab.getPosition()).setIcon(tabIcons[tab.getPosition()]);
            }
            if (mFragment != null)
            {
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.TabActivity tab, FragmentTransaction ft) {

        }
}*/


}
