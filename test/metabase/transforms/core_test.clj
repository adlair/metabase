(ns metabase.transforms.core-test
  (:require [expectations :refer [expect]]
            [metabase.models
             [card :refer [Card]]
             [table :as table :refer [Table]]]
            [metabase.query-processor :as qp]
            [metabase.test
             [automagic-dashboards :refer [with-rasta]]
             [data :as data]
             [domain-entities :refer :all]
             [transforms :refer :all]
             [util :as tu]]
            [metabase.transforms.core :as t]))

;; Run the transform and make sure it produces the correct result
(expect
  [[4 1 10.0646 -165.374 "Red Medicine" 3 1 4 3 2 1]
   [11 2 34.0996 -118.329 "Stout Burgers & Beers" 2 2 11 2 1 1]
   [11 3 34.0406 -118.428 "The Apple Pan" 2 2 11 2 1 1]]
  (with-rasta
    (with-test-domain-entity-specs
      (tu/with-model-cleanup ['Card 'Collection]
        (-> (t/apply-transform! (data/id) "PUBLIC" test-transform-spec)
            first
            :dataset_query
            qp/process-query
            :data
            :rows)))))


(expect
  "Test transform"
  (with-test-transform-specs
    (with-test-domain-entity-specs
      (-> (t/candidates (Table (data/id :venues)))
          first
          :name))))
