package com.crazymike.trace.writer;

import com.crazymike.respositories.ProductRepository;

public class GAWriter extends BaseWriter {

    private String params;

    public GAWriter(String url) {
        this.params = getParams(url).length() > 0
                ? String.format(".%s", getParams(url).replace("=", "-").replace("&", "."))
                : "";
    }

    public String getLaunchMessage() {
        return String.format("Launch%s", params);
    }

    public String getTagMessage(int position) {
        if (position == 0) {
            return String.format("channel-5%s", params);
        }else {
            return String.format("%s-%s%s",
                    getTargetWithTag(ProductRepository.getInstance()
                            .getUpSideMenus().get(position)
                            .getUrl().split("tag-")[1].split("/")[0]),
                    ProductRepository.getInstance().getUpSideMenus().get(position).getName(),
                    params);
        }
    }

    public String getItemMessage(String itemId) {
        return String.format("%s%s",
                getTargetWithItem(itemId),
                params);
    }

    public String getSearchMessage(String keyWord) {
        return String.format("%s%s",
                getTargetWithKeyword(keyWord),
                params);
    }
}
