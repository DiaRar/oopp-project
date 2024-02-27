/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.database;

import commons.Debt;
import commons.Participant;
import commons.primary_keys.DebtPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface DebtRepository extends JpaRepository<Debt, DebtPK> {
    Collection<Debt> findDebtsByPayerId(UUID id);
    Collection<Debt> findDebtsByPayer(Participant payer);
    Collection<Debt> findDebtsByDebtorId(UUID id);
    Collection<Debt> findDebtsByDebtor(Participant debtor);
}