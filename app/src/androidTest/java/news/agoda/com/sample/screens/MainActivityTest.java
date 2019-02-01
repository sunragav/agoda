package news.agoda.com.sample.screens;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import news.agoda.com.sample.R;
import news.agoda.com.sample.TestApplication;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.network.MockWebServiceDataInterator;
import news.agoda.com.sample.test.AsyncTaskSchedulerRule;
import news.agoda.com.sample.utils.JSONUtils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);
    private JSONArray jsonArray;
    private List<NewsEntity> newsEntities;
    private TestApplication application;
    MockWebServiceDataInterator dataInterator;

    enum INDEX {
        DEFAULT,//indexof fake json results array with all contents present for the NewsEntity Model
        TITLE,//indexof fake json results array with title field missing for the NewsEntity Model
        ABSTRACT,//indexof fake json results array with abstract field missing for the NewsEntity Model
        URL,//indexof fake json results array with url field missing for the NewsEntity Model
        MEDIA,//indexof fake json results array with multimedia field for the NewsEntity Model
        ALL//indexof fake json results array with all fields of NewsEntity Model missing
    }


    @Before
    public void setup() throws JSONException, IOException {
        Context context = InstrumentationRegistry.getContext();
        jsonArray = JSONUtils.fromStream(context.getAssets()
                .open("nullTest.json"));

        application = (TestApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        dataInterator = (MockWebServiceDataInterator) application.provideNetworkDataInteractor();
        dataInterator.setResult(jsonArray);
        newsEntities = JSONUtils.transformJsonToEntity(jsonArray);
        activityActivityTestRule.launchActivity(null);
    }

    void checkDataAtIndexInTheNewsListView(int pos) {
        onData(anything())
                .inAdapterView(withId(R.id.news_list_view))
                .atPosition(pos)
                .onChildView(withId(R.id.news_title))
                .check(matches(withText(newsEntities.get(pos).getTitle())));
    }

    void clickAtIndexInTheNewsListView(int pos) {
        onData(anything())
                .inAdapterView(withId(R.id.news_list_view))
                .onChildView(withId(R.id.news_title))
                .atPosition(pos)
                .perform(click());
    }

    @Test
    public void testListLoadPortrait() throws JSONException {

        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        for (INDEX e : INDEX.values()) {
            checkDataAtIndexInTheNewsListView(e.ordinal());
            clickAtIndexInTheNewsListView(e.ordinal());
            onView(withId(R.id.detailed_news_title)).check(matches(withText(newsEntities.get(e.ordinal()).getTitle())));
            pressBack();
            checkDataAtIndexInTheNewsListView(e.ordinal());
        }
        checkButtonInvisibleWhenArticleURLIsMissing();
        checkImageVisibleWhenArticleImageURLIsMissing();

        try {
            pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }
    }

    @Test
    public void checkButtonInvisibleWhenArticleURLIsMissing() {
        //When article URL missing Button should be invisible

        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        clickAtIndexInTheNewsListView(INDEX.URL.ordinal());
        onView(withId(R.id.full_story_link)).check(matches(not(isDisplayed())));
    }

    @Test
    public void checkImageVisibleWhenArticleImageURLIsMissing() {
        //When article URL missing Button should be invisible

        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        clickAtIndexInTheNewsListView(INDEX.MEDIA.ordinal());
        onView(withId(R.id.detailed_news_image)).check(matches((isDisplayed())));
    }


    @Test
    public void testListLoadLandScape() throws JSONException {

        activityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        for (INDEX e : INDEX.values()) {
            clickAtIndexInTheNewsListView(e.ordinal());
            onView(withId(R.id.detailed_news_title)).check(matches(withText(newsEntities.get(e.ordinal()).getTitle())));
        }

        checkButtonInvisibleWhenArticleURLIsMissing();
        checkImageVisibleWhenArticleImageURLIsMissing();
        try {
            pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }

    }

    @After
    public void end() {
        if (activityActivityTestRule.getActivity() != null)
            activityActivityTestRule.getActivity().finish();
    }
}