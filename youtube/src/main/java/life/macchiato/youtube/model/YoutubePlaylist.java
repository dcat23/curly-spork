package life.macchiato.youtube.model;

import com.google.api.services.youtube.model.SearchResult;


public class YoutubePlaylist extends YoutubeItem {

    public YoutubePlaylist(builder builder) {

    }

    public static class builder extends YoutubeItem.builder<builder, YoutubePlaylist> {

        @Override
        protected builder self() {
            return this;
        }

        @Override
        YoutubePlaylist build() {
            return new YoutubePlaylist(this);
        }
    }
    public static YoutubePlaylist from(SearchResult item) {
        return null;
    }

}
