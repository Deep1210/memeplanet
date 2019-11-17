
package in.incourt.incourtnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.models.SyncModel;
import in.incourt.incourtnews.helpers.IncourtLoader;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.MyInterestHelper;
import in.incourt.incourtnews.helpers.NetworkHelper;
import in.incourt.incourtnews.helpers.NoNetWorkStateHelper;
import in.incourt.incourtnews.helpers.PostListTypeHelper;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;
import in.incourt.incourtnews.layouts.PostLayoutPage;
import in.incourt.incourtnews.newslist.adapters.NewsListVerticalViewPagerAdapter;
import in.incourt.incourtnews.pagers.LandingScreenViewPager;

/**
 * Created by bhavan on 3/4/17.
 */

public class LandingScreenViewPagerAdapter extends NewsListVerticalViewPagerAdapter implements NoNetworkHelperInterface{

    int SLIDE_COUNTS = 4;

    int MY_FEED_SELECT_COUNT = 0;

    ViewGroup container;
    LayoutInflater landing_list_inflater;
    View itemView;
    PostLayoutPage postLayoutPage;
    LandingScreenViewPager landingScreenViewPager;

    IncourtLoader incourtLoader;

    ArrayList<Integer> my_feed_array = new ArrayList<>();

    public LandingScreenViewPagerAdapter(LandingScreenViewPager landingScreenViewPager, Context context) {
        super();
        postLayoutPage = new PostLayoutPage(this);
        landing_list_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.landingScreenViewPager = landingScreenViewPager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public int getCount() {
        return SLIDE_COUNTS;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container = container;
        itemView = showGuidLines(container, position);
        container.addView(itemView);
        return itemView;
    }


    View showGuidLines(ViewGroup container, int position) {

        int layout_id = 0;

        if (position == PostListTypeHelper.GUIDE_LINE_SIX_TEEN_WORD_SCREEN) {
            layout_id = in.incourt.incourtnews.R.layout.guidline_sixteenwords_1;
        } else if (position == PostListTypeHelper.GUIDE_LINE_SEARCH_SCREEN) {
            layout_id = in.incourt.incourtnews.R.layout.guidline_search_2;
        } else if (position == PostListTypeHelper.GUIDE_LINE_TYPIFY_SCREEN) {
            layout_id = in.incourt.incourtnews.R.layout.guidline_typify_3;
        } else if (position == PostListTypeHelper.GUIDE_LINE_LOADING_NEWS) {
            if (!NetworkHelper.state()) // || !SyncModel.getFirstSyncStatus()
                layout_id = in.incourt.incourtnews.R.layout.no_network_state;
            else
                layout_id = in.incourt.incourtnews.R.layout.loading_news;
        }

        itemView = landing_list_inflater.inflate(layout_id, container, false);

        if (layout_id == in.incourt.incourtnews.R.layout.no_network_state) {
            new NoNetWorkStateHelper(itemView, this);
        }

        return itemView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setSLIDE_COUNTS(int SLIDE_COUNTS) {
        this.SLIDE_COUNTS = SLIDE_COUNTS;
    }

    public int getSLIDE_COUNTS() {
        return SLIDE_COUNTS;
    }

    @Override
    public void onClickTryAgain() {
        if (NetworkHelper.state()) {
            incourtLoader = IncourtLoader.init(getLandingScreenViewPager().getContext()).start();

            SyncModel.syncFirstTime();

            final int[] index = {0};
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (index[0] <= 12) {
                        index[0]++;

                        if (index[0] == 7) {
                            IncourtApplication.getIncourtLauncherActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    IncourtToastHelprer.showTakingBitLong();
                                }
                            });
                        }
                        if (index[0] >= 12) {
                            IncourtApplication.getIncourtLauncherActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    IncourtToastHelprer.showNoNetwork();
                                    incourtLoader.stop();
                                }
                            });
                            break;
                        }
                        if (SyncModel.getFirstSyncStatus()) {
                            IncourtApplication.getIncourtLauncherActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    laodCategories();
                                }
                            });
                            break;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            IncourtToastHelprer.showNoNetwork();
        }
    }

    private void laodCategories() {
        incourtLoader.stop();
        landingScreenViewPager.setAdapter(landingScreenViewPager.getAdapter());
        landingScreenViewPager.setCurrentItem(4);
    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }

    void setupDoneButton(View itemView) {
        if (itemView != null) {
            itemView.findViewById(R.id.my_feed_interest_done_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MY_FEED_SELECT_COUNT < 3) {
                        IncourtToastHelprer.choseAtLeastThreeIntrest();
                    } else {
                        SLIDE_COUNTS = 5;
                        notifyDataSetChanged();
                        MyInterestHelper.save(my_feed_array);
                        landingScreenViewPager.setCurrentItem(4, true);
                    }
                }
            });
        }
    }


    public LandingScreenViewPager getLandingScreenViewPager() {
        return landingScreenViewPager;
    }
}
