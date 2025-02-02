package com.musicservice.elasticsearch.repository;

import com.musicservice.elasticsearch.documents.SongDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongElasticRepository extends ElasticsearchRepository<SongDoc, Integer> {

    @Query("""
    {
      "bool": {
        "should": [
          {
            "match": {
              "title": {
                "query": "?0",
                "fuzziness": "AUTO",
                "minimum_should_match": "50%"
              }
            }
          },
          {
            "prefix": {
              "title": {
                "value": "?0"
              }
            }
          }
        ],
        "minimum_should_match": 1
      }
    }
    """)
    Page<SongDoc> searchSongsByTitle(String query, Pageable pageable);
}
