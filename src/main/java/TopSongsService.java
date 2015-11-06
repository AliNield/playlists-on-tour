import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TopSongsService {

    private final static String SETLIST_FM_URL = "http://www.setlist.fm/";
    private static final String BAND_STATS_PAGE_HREF = "body > div.body > div.container > div.row.main > div.col-xs-12.col-md-3 > div:nth-child(1) > div.hidden-xs.hidden-sm > div > div:nth-child(2) > ul > li:nth-child(2) > a";

    public List<String> getTopSongs(String bandName, int playlistLength) throws IOException {
        String statsLink = getBandStatsLink(bandName);

        Document document = Jsoup.connect(SETLIST_FM_URL + statsLink).get();


        Elements songElements = document.select(".songName .songName");

        return songElements.stream()
                .map(Element::text)
                .collect(Collectors.toList())
                .subList(0, playlistLength);
    }

    private String getBandStatsLink(String bandName) throws IOException {
        Document bandSearchPage = Jsoup.connect(SETLIST_FM_URL + "search?query=" + URLEncoder.encode(bandName, "UTF-8")).get();
        return bandSearchPage.select(BAND_STATS_PAGE_HREF).attr("href");
    }

    public static void main(String[] args) throws IOException {
        new TopSongsService().getTopSongs("muse", 5).forEach(System.out::println);
    }

}
