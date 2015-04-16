package fractal.crud_rest_json.rest;

import java.util.ArrayList;
import java.util.List;

public abstract class Either<L, R> {

	public static class Left<L, R> extends Either<L, R> {
		private final L	value;

		public Left(L value) {
			this.value = value;
		}

		public L get() {
			return value;
		}
	}

	public static class Right<L, R> extends Either<L, R> {
		private final R	value;

		public Right(R value) {
			this.value = value;
		}

		public R get() {
			return value;
		}
	}

	public static void main(String[] args) {
		List<Either<String, Long>> eithers = new ArrayList<>();

		eithers.add(new Left<String, Long>("some string"));
		eithers.add(new Right<String, Long>(7L));

		for (Either<String, Long> either : eithers) {
			if (either instanceof Left) {
				System.out.println("String is: " + ((Left<String, Long>) either).get());
			} else if (either instanceof Right) {
				System.out.println("Long is: " + ((Right<String, Long>) either).get());
			}
		}

	}

}
