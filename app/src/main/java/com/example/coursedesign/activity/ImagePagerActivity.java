package com.example.coursedesign.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.example.coursedesign.R;
import com.example.coursedesign.adapter.ImagePagerAdapter;
import com.example.coursedesign.widget.HackyViewPager;

/**
 * 启动界面传递三个参数
 * 1.图片路径列表：IMAGE_PATHS
 * 2.图片来源：本地（1）或网络（2）IMAGE_FROM
 * 3.第一张图片位于列表中的第几项：IMAGE_INDEX_FIRST
 * usage：见ImageUtils的browerBigImage（）函数
 * 
 * 图片查看器
 */
public class ImagePagerActivity extends BaseFragmentActivity implements OnPageChangeListener
{
    /**
     * 常量
     */
    //图片位置
    private static final String STATE_POSITION = "STATE_POSITION";
    //图片路径列表数据
    public static final String IMAGE_PATHS = "image_paths";
    //图片来源
    public static final String IMAGE_FROM = "image_from";
    public static final int IMAGE_FROM_LOCAL = 1;
    public static final int IMAGE_FROM_NET = 2;
    //首张图片位置
    public static final String IMAGE_INDEX_FIRST= "image_index_first";

    /**
     * 界面资源
     */
    //显示图片张数
    private TextView indicator;

    
    /**
     * 变量
     */
    //图片路径数据
    private ArrayList<String> imagePaths;
    //图片来源
    private int imageFrom;
    // 首张图片的位置
    private int imageIndexFirst;
    
    private ImagePagerAdapter mAdapter;
    private HackyViewPager mPager;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        if (savedInstanceState != null)
        {
            imageIndexFirst = savedInstanceState.getInt(STATE_POSITION);
        }
        initVariables();
        initViews();
        initData();
    }


    /**
     * 初始化变量
     */
    private void initVariables()
    {
        //初始化传递过来的参数
        imagePaths = getIntent().getStringArrayListExtra(IMAGE_PATHS);
        imageFrom = getIntent().getIntExtra(IMAGE_FROM, 1);
        imageIndexFirst = getIntent().getIntExtra(IMAGE_INDEX_FIRST, 0);
        //初始化控件
        mPager = (HackyViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.indicator);
        if(imagePaths != null)
        {
            mAdapter = new ImagePagerAdapter(getSupportFragmentManager() , imagePaths , imageFrom);
        }
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);
        mPager.setCurrentItem(imageIndexFirst);
    }
    
    /**
     * 初始化界面
     */
    private void initViews()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 初始化数据
     */
    private void initData()
    {
        updateIndicator(imageIndexFirst);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }
    
    /**
     * 更新显示到第几张
     */
    protected void updateIndicator(int index)
    {
        CharSequence text = getString(R.string.viewpager_indicator, index+1, mPager.getAdapter().getCount());
        indicator.setText(text);
    }


    
    @Override
    public void onPageScrollStateChanged(int arg0)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
        // TODO Auto-generated method stub
        
    }

    //滑动到第几张
    @Override
    public void onPageSelected(int position)
    {
        updateIndicator(position);
    }


}
