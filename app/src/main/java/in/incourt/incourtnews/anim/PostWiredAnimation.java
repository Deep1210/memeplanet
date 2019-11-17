package in.incourt.incourtnews.anim;

import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.sql.PostsSql;
import in.incourt.incourtnews.layouts.CircleAnimationView;
import in.incourt.incourtnews.newslist.pager.NewsListVerticalViewPager;

/**
 * Created by bhavan on 3/20/17.
 */

public class PostWiredAnimation implements Animation.AnimationListener{

    private View itemView;
    private PostsSql postsSql;
    private CircleAngleAnimation circleAngleAnimation;
    NewsListVerticalViewPager newsListVerticalViewPager;

    private boolean status = true;

    public static PostWiredAnimation getInstance(){
        return new PostWiredAnimation();
    }

    public void getWired(View itemView, PostsSql postsSql){
        setItemView(itemView).setCircleAngleAnimation().setPostsSql(postsSql).startAnimation();
    }

    PostWiredAnimation startAnimation(){
        getItemView().findViewById(R.id.bottom_wired_button).setVisibility(View.GONE);
        changeVisibility(View.VISIBLE);
        getCircleAngleAnimation().setFillAfter(true);
        getCircleAnimationView().startAnimation(getCircleAngleAnimation());
        return this;
    }

    CircleAnimationView getCircleAnimationView(){
        return (CircleAnimationView) getItemView().findViewById(in.incourt.incourtnews.R.id.circle_animation);
    }

    PostWiredAnimation setCircleAngleAnimation(){
        circleAngleAnimation = new CircleAngleAnimation(getCircleAnimationView(), 360);
        circleAngleAnimation.setDuration(3200);
        circleAngleAnimation.setAnimationListener(this);
        return this;
    }

    public CircleAngleAnimation getCircleAngleAnimation() {
        return circleAngleAnimation;
    }

    public PostsSql getPostsSql() {
        return postsSql;
    }

    public PostWiredAnimation setPostsSql(PostsSql postsSql) {
        postsSql.setIs_wired(1);
        this.postsSql = postsSql;
        return this;
    }

    public NewsListVerticalViewPager getNewsListVerticalViewPager() {
        return newsListVerticalViewPager;
    }

    public PostWiredAnimation setNewsListVerticalViewPager(NewsListVerticalViewPager newsListVerticalViewPager) {
        this.newsListVerticalViewPager = newsListVerticalViewPager;
        return this;
    }

    View getItemView(){
        return itemView;
    }

    public PostWiredAnimation setItemView(View itemView) {
        this.itemView = itemView;
        return this;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(isStatus()) {

            getItemView().findViewById(R.id.bottom_wired_button).setVisibility(View.GONE);
            getItemView().findViewById(R.id.wired_cross_image).setVisibility(View.INVISIBLE);
            getItemView().findViewById(R.id.wired_status_main_layout).setVisibility(View.VISIBLE);
            getItemView().findViewById(R.id.wirechecked).setVisibility(View.VISIBLE);

            PostsSql.getWired(getPostsSql());
            getNewsListVerticalViewPager().updateCurrentRecord(0, getPostsSql());

        }
        setStatus(true);
    }


    public void clearAnimation(){
        changeVisibility(View.GONE);
        getCircleAnimationView().clearAnimation();
        getCircleAnimationView().refreshDrawableState();
        getCircleAnimationView().invalidate();
        if(!isStatus()) {
            getItemView().findViewById(in.incourt.incourtnews.R.id.bottom_wired_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setCircleAngleAnimation(CircleAngleAnimation circleAngleAnimation) {
        this.circleAngleAnimation = circleAngleAnimation;
    }

    public boolean isStatus() {
        return status;
    }

    public PostWiredAnimation setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public void checkState(View itemView, PostsSql postsSql) {
        if(postsSql.getIs_wired() > 1){
            showCounts(itemView, postsSql.getIs_wired());
        }else if(postsSql.getIs_wired() == 1){
            showCheckWired(itemView);
        }
    }

    public void showCounts(View itemView, int count){
        showCheckWired(itemView);
        TextView textView = (TextView) itemView.findViewById(in.incourt.incourtnews.R.id.wirecount);
        textView.setText(filterCount(count));
        textView.setVisibility(View.VISIBLE);
    }

    public String filterCount(int count) {
        if(count <= 999) return String.valueOf(count);
        return String.valueOf((count/1000)) + "k";
    }

    public void showCheckWired(View itemView){
        itemView.findViewById(R.id.wired_status_main_layout).setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.wirechecked).setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.bottom_wired_button).setVisibility(View.GONE);
    }

    void changeVisibility(int v){
        getItemView().findViewById(R.id.wired_status_main_layout).setVisibility(v);
        animationViewVisibility(v);
    }

    void animationViewVisibility(int v){
        getItemView().findViewById(R.id.wired_cross_image).setVisibility(v);
        getCircleAnimationView().setVisibility(v);
    }
}
