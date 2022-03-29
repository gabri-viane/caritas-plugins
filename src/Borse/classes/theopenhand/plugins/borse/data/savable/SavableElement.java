/*
 * Copyright 2022 gabri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theopenhand.plugins.borse.data.savable;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author gabri
 */
public class SavableElement implements Serializable {

    private BigInteger id;
    private Integer qnt;

    public SavableElement() {
    }

    public SavableElement(BigInteger id, Integer qnt) {
        this.id = id;
        this.qnt = qnt;
    }

    public BigInteger getId() {
        return id;
    }

    public Integer getQnt() {
        return qnt;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setQnt(Integer qnt) {
        this.qnt = qnt;
    }

}
