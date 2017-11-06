
package com.resurrect.filmpopuler.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MoviesList {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<Movies> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public List<Movies> getResults() {
        return mResults;
    }

    public void setResults(List<Movies> results) {
        mResults = results;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
