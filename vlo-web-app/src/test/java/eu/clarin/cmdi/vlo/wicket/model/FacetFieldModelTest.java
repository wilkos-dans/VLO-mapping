/*
 * Copyright (C) 2014 CLARIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.clarin.cmdi.vlo.wicket.model;

import eu.clarin.cmdi.vlo.pojo.QueryFacetsSelection;
import eu.clarin.cmdi.vlo.service.solr.FacetFieldsService;
import java.util.Arrays;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jmock.Expectations;
import static org.jmock.Expectations.returnValue;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author twagoo
 */
public class FacetFieldModelTest {

    private final Mockery context = new JUnit4Mockery();

    private IModel<QueryFacetsSelection> selectionModel;
    private QueryFacetsSelection selection;
    private FacetFieldsService service;

    @Before
    public void setUp() {
        service = context.mock(FacetFieldsService.class);
        selection = new QueryFacetsSelection();
        selectionModel = new Model(selection);
    }

    /**
     * Test of load method, of class FacetFieldsModel.
     */
    @Test
    public void testGetObject() {
        final FacetFieldModel instance = new FacetFieldModel(service, "facet4", selectionModel, 20);

        context.checking(new Expectations() {
            {
                oneOf(service).getFacetFields(selection, 20);
                will(returnValue(Arrays.asList(
                        new FacetField("facet1"),
                        new FacetField("facet2"),
                        new FacetField("facet3"),
                        new FacetField("facet4")
                )));
            }
        });

        final FacetField result = instance.getObject();
        // selection should be returned
        assertEquals("facet4", result.getName());
        
        // make another call, should not trigger a call to service because model is loadabledetachable
        final FacetField result2 = instance.getObject();
        assertEquals(result, result2);
    }
    
    /**
     * Test of load method, of class FacetFieldsModel.
     */
    @Test
    public void testGetObjectNotIncluded() {
        final FacetFieldModel instance = new FacetFieldModel(service, "facet4", selectionModel, 20);

        context.checking(new Expectations() {
            {
                oneOf(service).getFacetFields(selection, 20);
                will(returnValue(Arrays.asList(
                        new FacetField("facet1"),
                        new FacetField("facet2")
                )));
            }
        });

        final FacetField result = instance.getObject();
        // selection should be returned
        assertNull(result);
    }

}
