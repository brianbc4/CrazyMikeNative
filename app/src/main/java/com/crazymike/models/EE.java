package com.crazymike.models;

import java.util.List;

public class EE {

    private String Platform;
    private String PlatformContent;
    private String StoreDisplayName;
    private int TicketType;
    private String Refresh;
    private String TicketInfo;
    private String TicketDisplayStatus;
    private TicketData TicketData;

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String Platform) {
        this.Platform = Platform;
    }

    public String getPlatformContent() {
        return PlatformContent;
    }

    public void setPlatformContent(String PlatformContent) {
        this.PlatformContent = PlatformContent;
    }

    public String getStoreDisplayName() {
        return StoreDisplayName;
    }

    public void setStoreDisplayName(String StoreDisplayName) {
        this.StoreDisplayName = StoreDisplayName;
    }

    public int getTicketType() {
        return TicketType;
    }

    public void setTicketType(int TicketType) {
        this.TicketType = TicketType;
    }

    public String getRefresh() {
        return Refresh;
    }

    public void setRefresh(String Refresh) {
        this.Refresh = Refresh;
    }

    public String getTicketInfo() {
        return TicketInfo;
    }

    public void setTicketInfo(String TicketInfo) {
        this.TicketInfo = TicketInfo;
    }

    public String getTicketDisplayStatus() {
        return TicketDisplayStatus;
    }

    public void setTicketDisplayStatus(String TicketDisplayStatus) {
        this.TicketDisplayStatus = TicketDisplayStatus;
    }

    public TicketData getTicketData() {
        return TicketData;
    }

    public void setTicketData(TicketData TicketData) {
        this.TicketData = TicketData;
    }

    public static class TicketData {
        private HeaderField HeaderField;
        private MainField MainField;
        private FootField FootField;
        private List<SecondField> SecondField;

        public HeaderField getHeaderField() {
            return HeaderField;
        }

        public void setHeaderField(HeaderField HeaderField) {
            this.HeaderField = HeaderField;
        }

        public MainField getMainField() {
            return MainField;
        }

        public void setMainField(MainField MainField) {
            this.MainField = MainField;
        }

        public FootField getFootField() {
            return FootField;
        }

        public void setFootField(FootField FootField) {
            this.FootField = FootField;
        }

        public List<SecondField> getSecondField() {
            return SecondField;
        }

        public void setSecondField(List<SecondField> SecondField) {
            this.SecondField = SecondField;
        }

        public static class HeaderField {
            private String HeaderLogo;
            private String HeaderTitle;
            private String HeaderRightLabel;
            private String HeaderRightValue;

            public String getHeaderLogo() {
                return HeaderLogo;
            }

            public void setHeaderLogo(String HeaderLogo) {
                this.HeaderLogo = HeaderLogo;
            }

            public String getHeaderTitle() {
                return HeaderTitle;
            }

            public void setHeaderTitle(String HeaderTitle) {
                this.HeaderTitle = HeaderTitle;
            }

            public String getHeaderRightLabel() {
                return HeaderRightLabel;
            }

            public void setHeaderRightLabel(String HeaderRightLabel) {
                this.HeaderRightLabel = HeaderRightLabel;
            }

            public String getHeaderRightValue() {
                return HeaderRightValue;
            }

            public void setHeaderRightValue(String HeaderRightValue) {
                this.HeaderRightValue = HeaderRightValue;
            }
        }

        public static class MainField {
            private String Img;
            private List<FirstDisplay> FirstDisplay;

            public String getImg() {
                return Img;
            }

            public void setImg(String Img) {
                this.Img = Img;
            }

            public List<FirstDisplay> getFirstDisplay() {
                return FirstDisplay;
            }

            public void setFirstDisplay(List<FirstDisplay> FirstDisplay) {
                this.FirstDisplay = FirstDisplay;
            }

            public static class FirstDisplay {
                private String ItemLabel;
                private String ItemValue;

                public String getItemLabel() {
                    return ItemLabel;
                }

                public void setItemLabel(String ItemLabel) {
                    this.ItemLabel = ItemLabel;
                }

                public String getItemValue() {
                    return ItemValue;
                }

                public void setItemValue(String ItemValue) {
                    this.ItemValue = ItemValue;
                }
            }
        }

        public static class FootField {
            private String BarcodeType;
            private String CodeData;

            public String getBarcodeType() {
                return BarcodeType;
            }

            public void setBarcodeType(String BarcodeType) {
                this.BarcodeType = BarcodeType;
            }

            public String getCodeData() {
                return CodeData;
            }

            public void setCodeData(String CodeData) {
                this.CodeData = CodeData;
            }
        }

        public static class SecondField {
            private String ItemLabel;
            private String ItemValue;

            public String getItemLabel() {
                return ItemLabel;
            }

            public void setItemLabel(String ItemLabel) {
                this.ItemLabel = ItemLabel;
            }

            public String getItemValue() {
                return ItemValue;
            }

            public void setItemValue(String ItemValue) {
                this.ItemValue = ItemValue;
            }
        }
    }
}
