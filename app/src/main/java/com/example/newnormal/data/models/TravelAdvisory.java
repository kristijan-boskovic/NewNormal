package com.example.newnormal.data.models;

import java.util.Map;

public class TravelAdvisory {
    private final ApiStatus api_status;
    private final Map<String, CountryData> data;

    public TravelAdvisory(ApiStatus api_status, Map<String, CountryData> data) {
        this.api_status = api_status;
        this.data = data;
    }

    public ApiStatus getApiStatus() {
        return api_status;
    }

    public Map<String, CountryData> getData() {
        return data;
    }


    private static class ApiStatus {
        private final Request request;
        private final Reply reply;

        public ApiStatus(Request request, Reply reply) {
            this.request = request;
            this.reply = reply;
        }

        public Request getRequest() {
            return request;
        }

        public Reply getReply() {
            return reply;
        }


        private static class Request {
            private final String item;

            public Request(String item) {
                this.item = item;
            }

            public String getItem() {
                return item;
            }
        }

        private static class Reply {
            private final String cache;
            private final String code;
            private final String status;
            private final String note;
            private final String count;

            public Reply(String cache, String code, String status, String note, String count) {
                this.cache = cache;
                this.code = code;
                this.status = status;
                this.note = note;
                this.count = count;
            }

            public String getCache() {
                return cache;
            }

            public String getCode() {
                return code;
            }

            public String getStatus() {
                return status;
            }

            public String getNote() {
                return note;
            }

            public String getCount() {
                return count;
            }
        }
    }

    private static class CountryData {
        private final String iso_alpha2;
        private final String name;
        private final String continent;
        private final Advisory advisory;

        public CountryData(String iso_alpha2, String name, String continent, Advisory advisory) {
            this.iso_alpha2 = iso_alpha2;
            this.name = name;
            this.continent = continent;
            this.advisory = advisory;
        }

        public String getIso_alpha2() {
            return iso_alpha2;
        }

        public String getName() {
            return name;
        }

        public String getContinent() {
            return continent;
        }

        public Advisory getAdvisory() {
            return advisory;
        }


        private static class Advisory {
            private final String score;
            private final String sources_active;
            private final String message;
            private final String updated;
            private final String source;

            public Advisory(String score, String sources_active, String message, String updated, String source) {
                this.score = score;
                this.sources_active = sources_active;
                this.message = message;
                this.updated = updated;
                this.source = source;
            }

            public String getScore() {
                return score;
            }

            public String getSources_active() {
                return sources_active;
            }

            public String getMessage() {
                return message;
            }

            public String getUpdated() {
                return updated;
            }

            public String getSource() {
                return source;
            }
        }
    }
}