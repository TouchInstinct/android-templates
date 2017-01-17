package ru.touchin.templates.pairs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Pair that needed for saving in state because it implements Serializable interface.
 * First argument must be not null and second one - nullable.
 * Note that if you want to save this pair in state, you need make TFirst and TSecond Serializable too.
 *
 * @param <TFirst> type of the first nonnull argument.
 * @param <TSecond> type of the second nullable argument.
 */
public class HalfNullablePair<TFirst, TSecond> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final TFirst first;
    @Nullable
    private final TSecond second;

    public HalfNullablePair(@NonNull final TFirst first, @Nullable final TSecond second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get first argument of this pair. It is always not null.
     */
    @NonNull
    public TFirst getFirst() {
        return first;
    }

    /**
     * Get second argument of this pair. It may be nullable.
     */
    @Nullable
    public TSecond getSecond() {
        return second;
    }

}
