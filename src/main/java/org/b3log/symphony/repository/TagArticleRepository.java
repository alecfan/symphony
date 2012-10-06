/*
 * Copyright (c) 2012, B3log Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.symphony.repository;

import java.util.List;
import java.util.logging.Logger;
import org.b3log.latke.Keys;
import org.b3log.latke.repository.*;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Tag-Article relation repository.
 *
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.0, Sep 28, 2012
 * @since 0.2.0
 */
public final class TagArticleRepository extends AbstractRepository {

    /**
     * Singleton.
     */
    private static final TagArticleRepository SINGLETON = new TagArticleRepository(Tag.TAG + "_" + Article.ARTICLE);

    /**
     * Gets tag-article relations by the specified article id.
     *
     * @param articleId the specified article id
     * @return for example
     * <pre>
     * [{
     *         "oId": "",
     *         "tag_oId": "",
     *         "article_oId": articleId
     * }, ....], returns an empty list if not found
     * </pre>
     * @throws RepositoryException repository exception
     */
    public List<JSONObject> getByArticleId(final String articleId) throws RepositoryException {
        final Query query = new Query().setFilter(
                new PropertyFilter(Article.ARTICLE + "_" + Keys.OBJECT_ID, FilterOperator.EQUAL, articleId)).
                setPageCount(1);

        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);

        return CollectionUtils.jsonArrayToList(array);
    }

    /**
     * Gets tag-article relations by the specified tag id.
     *
     * @param tagId the specified tag id
     * @param currentPageNum the specified current page number, MUST greater
     * then {@code 0}
     * @param pageSize the specified page size(count of a page contains objects),
     * MUST greater then {@code 0}
     * @return for example
     * <pre>
     * {
     *     "pagination": {
     *       "paginationPageCount": 88250
     *     },
     *     "rslts": [{
     *         "oId": "",
     *         "tag_oId": tagId,
     *         "article_oId": ""
     *     }, ....]
     * }
     * </pre>
     * @throws RepositoryException repository exception
     */
    public JSONObject getByTagId(final String tagId, final int currentPageNum, final int pageSize)
            throws RepositoryException {
        final Query query = new Query().setFilter(
                new PropertyFilter(Tag.TAG + "_" + Keys.OBJECT_ID, FilterOperator.EQUAL, tagId)).
                addSort(Article.ARTICLE + "_" + Keys.OBJECT_ID, SortDirection.DESCENDING).
                setCurrentPageNum(currentPageNum).
                setPageSize(pageSize).
                setPageCount(1);

        return get(query);
    }

    /**
     * Gets the {@link TagArticleRepository} singleton.
     *
     * @return the singleton
     */
    public static TagArticleRepository getInstance() {
        return SINGLETON;
    }

    /**
     * Private constructor.
     * 
     * @param name the specified name
     */
    private TagArticleRepository(final String name) {
        super(name);
    }
}