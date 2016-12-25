package com.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DensityUtils;
import com.example.xiaoshuai.topbardemo.R;

/**
 * Created by xiaoshuai on 2016/12/22.
 */

public class TopBar extends RelativeLayout implements View.OnClickListener {
    /********
     * 页面元素
     *******/
    private RelativeLayout containerBack;
    private ImageView back;
    private TextView backLabel;
    private TextView title;
    /*----右边默认可以放三个图标----*/
    private ImageView ivRightOne;
    private ImageView ivRightTwo;
    private ImageView ivRightThree;

    private int mBackMode = 0;
    private int mTitleMode = 0;
    private static final int BACKMODE_NORMAL = 0;
    private static final int BACKMODE_OTHER = 1;
    private static final int BACKMODE_GONE = 2;
    private static final int TITLEMODE_CENTER = 0;
    private static final int TITLEMODE_LEFT = 1;

    private String mTitleText;
    private int mTitleColor;
    private static final int DEFAULT_TEXT_COLOR = 0XFFFFFFFF;
    private BitmapDrawable backBg;
    private String mBackText;
    private int mBackTextColor;

    private int DEFAULT_SRC_EMPTY = 0X0;
    private BitmapDrawable ivRightOneBg;
    private BitmapDrawable ivRightTwoBg;
    private BitmapDrawable ivRightThreeBg;


    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnTitleClickListener {
        void onTitleClick();
    }

    public interface OnIvRightOneClickListener {
        void onIvRightOneClick();
    }

    public interface OnIvRightTwoClickListener {
        void onIvRightTwoClick();
    }

    public interface OnIvRightThreeClickListener {
        void onIvRightThreeClick();
    }

    private OnBackClickListener onBackClickListener;
    private OnTitleClickListener onTitleClickListener;
    private OnIvRightOneClickListener onIvRightOneClickListener;
    private OnIvRightTwoClickListener onIvRightTwoClickListener;
    private OnIvRightThreeClickListener onIvRightThreeClickListener;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        mBackMode=array.getInt(R.styleable.TopBar_backMode,BACKMODE_NORMAL);
        mTitleMode=array.getInt(R.styleable.TopBar_titleMode,TITLEMODE_CENTER);
        backBg = (BitmapDrawable) array.getDrawable(R.styleable.TopBar_backIcon);
        mBackText = array.getString(R.styleable.TopBar_backText);
        mBackTextColor = array.getColor(R.styleable.TopBar_backTextColor, DEFAULT_TEXT_COLOR);
        mTitleText = array.getString(R.styleable.TopBar_titleText);
        mTitleColor = array.getColor(R.styleable.TopBar_titleTextColor, DEFAULT_TEXT_COLOR);
        ivRightOneBg = (BitmapDrawable) array.getDrawable(R.styleable.TopBar_ivRightOneIcon);
        ivRightTwoBg = (BitmapDrawable) array.getDrawable(R.styleable.TopBar_ivRightTwoIcon);
        ivRightThreeBg = (BitmapDrawable) array.getDrawable(R.styleable.TopBar_ivRightThreeIcon);
        array.recycle();
        initViews();
        addAndLayoutViews();
        initDefaultEvent();
    }

    /**
     * 初始化view
     */
    private void initViews() {
        containerBack = new RelativeLayout(getContext());
        containerBack.setId(R.id.container_back);
        //返回
        back = new ImageView(getContext());
        back.setId(R.id.back);
        back.setScaleType(ImageView.ScaleType.FIT_XY);
        if (backBg == null) {
            back.setImageResource(R.drawable.back);
        } else {
            back.setImageBitmap(backBg.getBitmap());
        }

        //返回文字
        backLabel = new TextView(getContext());
        backLabel.setId(R.id.backlabel);
        backLabel.setText(mBackText);
        backLabel.setTextSize(DensityUtils.sp2px(getContext(),6));
        backLabel.setTextColor(mBackTextColor);
        //标题
        title = new TextView(getContext());
        title.setId(R.id.title);
        title.setText(mTitleText);
        title.setTextSize(DensityUtils.sp2px(getContext(),8));
        title.setTextColor(mTitleColor);
        //右边三个图标
        ivRightOne = new ImageView(getContext());
        ivRightOne.setScaleType(ImageView.ScaleType.FIT_XY);
        ivRightOne.setId(R.id.iv_right_one);
        checkIsShowIcon(ivRightOneBg, ivRightOne);

        ivRightTwo = new ImageView(getContext());
        ivRightTwo.setId(R.id.iv_right_two);
        ivRightTwo.setScaleType(ImageView.ScaleType.FIT_XY);
        checkIsShowIcon(ivRightTwoBg, ivRightTwo);

        ivRightThree = new ImageView(getContext());
        ivRightThree.setId(R.id.iv_right_three);
        ivRightThree.setScaleType(ImageView.ScaleType.FIT_XY);
        checkIsShowIcon(ivRightThreeBg, ivRightThree);
    }

    /**
     * 检查是否显示图标
     *
     * @param bd
     * @param imageView
     */
    private void checkIsShowIcon(BitmapDrawable bd, ImageView imageView) {
        if (bd == null) {
            //说明没有设置src 这时候没有必要显示ImageView
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageBitmap(bd.getBitmap());
        }
    }

    /**
     * 添加并布局view
     */
    private void addAndLayoutViews() {
        //这里设置了一些默认值，可以根据自己的需求进行调整，也可以抽成自定义属性，但是每个页面应该都是一样的，没有必要这样做

        //返回图标和返回文字
        LayoutParams p_back = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getContext(), 30), DensityUtils.dp2px(getContext(), 30));
        p_back.leftMargin = DensityUtils.dp2px(getContext(), 10);
        p_back.addRule(RelativeLayout.CENTER_VERTICAL);
        containerBack.addView(back, p_back);

        LayoutParams p_backLabel = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p_backLabel.addRule(RelativeLayout.CENTER_VERTICAL);
        p_backLabel.addRule(RelativeLayout.RIGHT_OF,back.getId());
        p_backLabel.leftMargin = DensityUtils.dp2px(getContext(), 5);
        containerBack.addView(backLabel, p_backLabel);

        LayoutParams p_container = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        p_container.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(containerBack, p_container);
        //标题
        LayoutParams p_title = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (mTitleMode == TITLEMODE_CENTER) {
            p_title.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if(mTitleMode==TITLEMODE_LEFT){
            p_title.addRule(RelativeLayout.CENTER_VERTICAL);
            p_title.leftMargin = DensityUtils.dp2px(getContext(), 10);
            p_title.addRule(RelativeLayout.RIGHT_OF, backLabel.getId());
        }
        addView(title, p_title);

        //右边的三个图标
        LayoutParams p_iv_right_one = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getContext(), 30), DensityUtils.dp2px(getContext(), 30));
        p_iv_right_one.addRule(RelativeLayout.CENTER_VERTICAL);
        p_iv_right_one.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p_iv_right_one.rightMargin = DensityUtils.dp2px(getContext(), 5);
        addView(ivRightOne, p_iv_right_one);

        LayoutParams p_iv_right_two = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getContext(), 30), DensityUtils.dp2px(getContext(), 30));
        p_iv_right_two.addRule(RelativeLayout.CENTER_VERTICAL);
        p_iv_right_two.addRule(RelativeLayout.LEFT_OF, ivRightOne.getId());
        p_iv_right_two.rightMargin = DensityUtils.dp2px(getContext(), 5);
        addView(ivRightTwo, p_iv_right_two);

        LayoutParams p_iv_right_three = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getContext(), 30), DensityUtils.dp2px(getContext(), 30));
        p_iv_right_three.addRule(RelativeLayout.CENTER_VERTICAL);
        p_iv_right_three.addRule(RelativeLayout.LEFT_OF, ivRightTwo.getId());
        p_iv_right_three.rightMargin = DensityUtils.dp2px(getContext(), 5);
        addView(ivRightThree, p_iv_right_three);
    }

    private void initDefaultEvent() {
        containerBack.setOnClickListener(this);
        title.setOnClickListener(this);
        ivRightOne.setOnClickListener(this);
        ivRightTwo.setOnClickListener(this);
        ivRightThree.setOnClickListener(this);
    }

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public void setOnIvRightOneClickListener(OnIvRightOneClickListener onIvRightOneClickListener) {
        this.onIvRightOneClickListener = onIvRightOneClickListener;
    }

    public void setOnIvRightTwoClickListener(OnIvRightTwoClickListener onIvRightTwoClickListener) {
        this.onIvRightTwoClickListener = onIvRightTwoClickListener;
    }

    public void setOnIvRightThreeClickListener(OnIvRightThreeClickListener onIvRightThreeClickListener) {
        this.onIvRightThreeClickListener = onIvRightThreeClickListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container_back:
                goBack();
                break;
            case R.id.title:
                onTitleClick();
                break;
            case R.id.iv_right_one:
                onIvRightOneClick();
                break;
            case R.id.iv_right_two:
                onIvRightTwoClick();
                break;
            case R.id.iv_right_three:
                onIvRightThreeClick();
                break;
        }
    }

    /**
     * 返回键被点击时候得处理
     */
    private void goBack() {
        if(onBackClickListener!=null){
            onBackClickListener.onBackClick();
            //如果设置了监听那么就不用进行后面的判断了
            return;
        }
        if (mBackMode == BACKMODE_NORMAL) {
            ((Activity) getContext()).finish();
        }else if(mBackMode==BACKMODE_GONE){
            containerBack.setVisibility(View.GONE);
        }
    }

    /**
     * 当标题被点击的时候得处理
     */
    private void onTitleClick(){
        if(onTitleClickListener!=null){
            onTitleClickListener.onTitleClick();
        }
    }

    private void onIvRightOneClick(){
        if (onIvRightOneClickListener!=null){
            onIvRightOneClickListener.onIvRightOneClick();
        }
    }

    private void onIvRightTwoClick(){
        if(onIvRightTwoClickListener!=null){
            onIvRightTwoClickListener.onIvRightTwoClick();
        }
    }

    private void onIvRightThreeClick(){
        if(onIvRightThreeClickListener!=null){
            onIvRightThreeClickListener.onIvRightThreeClick();
        }
    }
}
