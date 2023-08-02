package kr.co.dk.sample.api.common.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Paging {
    private int pageSize; // 현페이지당 갯수
    private int pageNo; // 현재 페이지 번호

    //ex) pageBlockSize = 10 : < 1 2 3 4 5 6 7 8 9 10 >
    //es) pageBlockSize = 5 :  < 1 2 3 4 5 >
    private int pageBlockSize;  // 페이징블럭 수  (페이징 네비 기준)

    private int startPageNo; // 시작 페이지 (페이징 네비 기준)
    private int endPageNo; // 끝 페이지 (페이징 네비 기준)

    private int firstPageNo; // 첫 번째 페이지 번호
    private int finalPageNo; // 마지막 페이지 번호


    private int prevPageNo; // 이전 페이지 번호
    private int nextPageNo; // 다음 페이지 번호

    private int totalCount; // 게시 글 전체 수
    private int startIndex; // 게시 글 Select Limit Start_index

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * @return the firstPageNo
     */
    public int getFirstPageNo() {
        return firstPageNo;
    }

    /**
     * @param firstPageNo the firstPageNo to set
     */
    public void setFirstPageNo(int firstPageNo) {
        this.firstPageNo = firstPageNo;
    }

    /**
     * @return the prevPageNo
     */
    public int getPrevPageNo() {
        return prevPageNo;
    }

    /**
     * @param prevPageNo the prevPageNo to set
     */
    public void setPrevPageNo(int prevPageNo) {
        this.prevPageNo = prevPageNo;
    }

    /**
     * @return the startPageNo
     */
    public int getStartPageNo() {
        return startPageNo;
    }

    /**
     * @param startPageNo the startPageNo to set
     */
    public void setStartPageNo(int startPageNo) {
        this.startPageNo = startPageNo;
    }

    /**
     * @return the pageNo
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * @param pageNo the pageNo to set
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return the endPageNo
     */
    public int getEndPageNo() {
        return endPageNo;
    }

    /**
     * @param endPageNo the endPageNo to set
     */
    public void setEndPageNo(int endPageNo) {
        this.endPageNo = endPageNo;
    }

    /**
     * @return the nextPageNo
     */
    public int getNextPageNo() {
        return nextPageNo;
    }

    /**
     * @param nextPageNo the nextPageNo to set
     */
    public void setNextPageNo(int nextPageNo) {
        this.nextPageNo = nextPageNo;
    }

    /**
     * @return the finalPageNo
     */
    public int getFinalPageNo() {
        return finalPageNo;
    }

    /**
     * @param finalPageNo the finalPageNo to set
     */
    public void setFinalPageNo(int finalPageNo) {
        this.finalPageNo = finalPageNo;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.makePaging();
    }

    /**
     * 페이징 생성
     */
    private void makePaging() {
        if (this.totalCount == 0) return; // 게시 글 전체 수가 없는 경우
        if (this.pageNo == 0) this.setPageNo(1); // 기본 값 설정
        if (this.pageSize == 0) this.setPageSize(10); // 기본 값 설정
        if (this.pageBlockSize == 0) this.setPageBlockSize(10); // 기본 값 설정


        int finalPage = (totalCount - 1) / pageSize + 1; //마지막페이지
        if (this.pageNo > finalPage) this.setPageNo(finalPage); // 기본 값 설정

        if (this.pageNo < 0 || this.pageNo > finalPage) this.pageNo = 1; // 현재 페이지 유효성 체크

        boolean isNowFirst = pageNo == 1; // 시작 페이지 (전체)
        boolean isNowFinal = pageNo == finalPage; // 마지막 페이지 (전체)

        int startPage = ((pageNo - 1) / pageBlockSize) * pageBlockSize + 1;  // 시작 페이지 (페이징 네비 기준)
        int endPage = startPage + pageBlockSize - 1; // 끝 페이지 (페이징 네비 기준)

        if (endPage > finalPage) { // [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우
            endPage = finalPage;
        }

        this.setFirstPageNo(1); // 첫 번째 페이지 번호

        if (isNowFirst) {
            this.setPrevPageNo(1); // 이전 페이지 번호
        } else {
            this.setPrevPageNo(((pageNo - 1) < 1 ? 1 : (pageNo - 1))); // 이전 페이지 번호
        }

        this.setStartPageNo(startPage); // 시작 페이지 (페이징 네비 기준)
        this.setEndPageNo(endPage); // 끝 페이지 (페이징 네비 기준)

        if (isNowFinal) {
            this.setNextPageNo(finalPage); // 다음 페이지 번호
        } else {
            this.setNextPageNo(((pageNo + 1) > finalPage ? finalPage : (pageNo + 1))); // 다음 페이지 번호
        }

        this.setFinalPageNo(finalPage); // 마지막 페이지 번호

        if (pageNo == 1) {
            this.startIndex = 0;
        } else {
            this.startIndex = (pageNo * pageSize) - pageSize;
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public int getPageBlockSize() {
        return pageBlockSize;
    }

    public void setPageBlockSize(int pageBlockSize) {
        this.pageBlockSize = pageBlockSize;
    }
}



