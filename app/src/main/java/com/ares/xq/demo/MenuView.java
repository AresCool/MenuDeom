package com.ares.xq.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *
 */

public class MenuView extends LinearLayout {

    //tab字体大小
    private int menuTextSize = 14;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    //是否显示最后一个ico,默认显示
    private boolean isShowLastIco = true;

    private int textColor = 0x666666;

    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    public MenuView(Context context) {
        super(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private MenuViewListner listener;

    public void setListener(MenuViewListner listener) {
        this.listener = listener;
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        textColor = a.getColor(R.styleable.DropDownMenu_textColor, textColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_textSize, menuTextSize);
        isShowLastIco = a.getBoolean(R.styleable.DropDownMenu_isLastShowIco, isShowLastIco);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_select_ico, R.drawable.filter_up);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_un_select_ico, R.drawable.filter_down);
        LogUtils.i("textColor：" + textColor + ",menuTextSize: " + menuTextSize + ",isShowLastIco: " + isShowLastIco);
        a.recycle();
    }

    /**
     * 设置菜单
     *
     * @param tabTexts
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts) {
        final int size = tabTexts.size();
        for (int i = 0; i < size; i++) {
            if (isShowLastIco) {
                addTextView(tabTexts.get(i), true);
            } else {
                if (i == size - 1) {
                    addTextView(tabTexts.get(i), false);
                } else {
                    addTextView(tabTexts.get(i), true);
                }
            }
        }
    }

    /**
     * 添加菜单到布局
     *
     * @param titlle
     */
    private void addTextView(final String titlle, boolean isShowIco) {
        LinearLayout rootItemView = new LinearLayout(getContext());
        rootItemView.setOrientation(LinearLayout.HORIZONTAL);
        rootItemView.setGravity(Gravity.CENTER);
        rootItemView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        rootItemView.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
        TextView tv = new TextView(getContext());
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tv.setTextColor(textColor);
        if (isShowIco) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
            tv.setCompoundDrawablePadding(dpTpPx(5));
        }
        tv.setText(titlle);
        rootItemView.addView(tv);
        rootItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("选中-->" + titlle);
                switchMenu(v);
            }
        });
        addView(rootItemView);
    }


    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        int childCount = getChildCount();
        LogUtils.i("当前共有childCount：" + childCount);
        for (int i = 0; i < childCount; i++) {
            if (target == getChildAt(i)) {
                if (current_tab_position == i) {
                    setTextBack(childCount, i, false);
                    if (current_tab_position != -1) {
                        current_tab_position = -1;
                    }
                    if (listener != null) {
                       listener.onClickCancle(i,target);
                    }
                } else {
                    setTextBack(childCount, i, true);
                    current_tab_position = i;
                    if (listener != null) {
                        listener.onClickMenuItem(i,target);
                    }
                }
            } else {
                setTextBack(childCount, i, false);
            }
        }
    }


    private void setTextBack(int count, int tag, boolean isUpOrDown) {
        if (isUpOrDown) {//select
            if (isShowLastIco) {
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuSelectedIcon), null);
            } else {
                if (tag == count - 1) {
                    ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                } else {
                    ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                    ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                }
            }
        } else {//unselect
            if (isShowLastIco) {
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
            } else {
                if (tag == count - 1) {
                    ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                } else {
                    ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuUnselectedIcon), null);
                }
            }

        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(int tag, String text) {
        if (tag != -1) {
            ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setText(text);
        }
    }

    /**
     * 关闭
     *
     * @param tag
     */
    public void closeItemMenu(int tag) {
        int childCount = getChildCount();
        if (isShowLastIco) {
            ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
            ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnselectedIcon), null);
        } else {
            if (tag == childCount - 1) {
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
            } else {
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setTextColor(textColor);
                ((TextView) ((LinearLayout) getChildAt(tag)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
            }
        }
        current_tab_position = -1;
    }

    /**
     * 获取展示的view的root
     *
     * @param tag
     * @return
     */
    public View getShowRootView(int tag) {
        return getChildAt(tag);
    }

    public interface MenuViewListner {
      //取消
        void onClickCancle(int tag,View view);

        //选中
        void onClickMenuItem(int tag,View view);
    }

}
